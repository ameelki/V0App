package com.protect.security_manager.service;

import com.protect.security_manager.Mapper.UserMapper;
import com.protect.security_manager.entity.UserEntity;
import com.protect.security_manager.exception.UserAlreadyExistException;
import com.protect.security_manager.exception.UserNotExistException;
import com.protect.security_manager.repository.UserRepository;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import security.manager.model.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private  Keycloak keycloak;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;
    public CreateUser200Response createUser(User user) {
        CreateUser200Response createUser200Response;
        UserRepresentation userRepresentation = convertToUserRepresentation(user);
        RealmResource realmResource = keycloak.realm("SecurityManager");
        UsersResource usersResource = realmResource.users();
        Response response = usersResource.create(userRepresentation);
        int statusCode = response.getStatus();

        switch (statusCode) {
            case 201:
                createUser200Response=handleUserCreationSuccess(response, user, realmResource, usersResource);
                break;
            case 409:
                throw new UserAlreadyExistException("User already exists");
            default:
                throw new UserAlreadyExistException("Problem while creating user Retry creation");
        }
        return createUser200Response;
    }

    private CreateUser200Response handleUserCreationSuccess(Response response, User user, RealmResource realmResource, UsersResource usersResource) {
        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        addClientRoleToUser(userId, realmResource, usersResource);

        // Create Address using the private method
        UserEntity.Address userAddress = createAddressFromUser(user);

        // Save user with address to database
        UserEntity userEntity=  saveUserToDatabase(userId,user,userAddress);
        CreateUser200Response createUser200Response= new CreateUser200Response();

        createUser200Response.setUser(this.userMapper.user(userEntity));
        createUser200Response.setPersonId(userEntity.getId());

        return createUser200Response;






    }
    private UserEntity.Address createAddressFromUser(User user) {
        UserEntity.Address address = new UserEntity.Address();
        address.setCity(user.getAddress().getCity());
        address.setStreet(user.getAddress().getStreet());
        address.setPostalCode(user.getAddress().getPostalCode());
        address.setCountry(user.getAddress().getCountry());
        return address;
    }

    private void addClientRoleToUser(String userId, RealmResource realmResource, UsersResource usersResource) {
        RoleRepresentation clientRole = realmResource.roles().get("simpleUser").toRepresentation();
        usersResource.get(userId).roles().realmLevel().add(Collections.singletonList(clientRole));
    }

    private UserEntity saveUserToDatabase(String Userid,User user,UserEntity.Address userAddress) {
        UserEntity savedUser ;
        try {
            UserEntity userEntity = userMapper.userToUserDto(user);
            userEntity.setId(Userid);
            userEntity.setAddress(userAddress);

        savedUser=this.userRepository.save(userEntity);


        } catch (DataIntegrityViolationException ex) {

                throw new UserAlreadyExistException(ex.getMessage());

        }
        return savedUser;
    }
    public UserRepresentation convertToUserRepresentation(User user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEmailVerified(false);
        // Combine all attributes into a single list
        Map<String, List<String>> attributesMap = createAttributesMap(user);
        userRepresentation.setAttributes(attributesMap);


        // Set all attributes in the UserRepresentation
        userRepresentation.setAttributes(attributesMap);
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false); // Le mot de passe n'est pas temporaire
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(user.getPassword());
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        return userRepresentation;
    }

    private String composeAddressString(User user) {
        if (user.getAddress() == null) {
            return "No address provided";
        }
        return String.format("%s, %s, %s, %s",
                user.getAddress().getPostalCode(),
                user.getAddress().getStreet(),
                user.getAddress().getCity(),
                user.getAddress().getCountry());
    }

    private Map<String, List<String>> createAttributesMap(User user) {
        Map<String, List<String>> attributesMap = new HashMap<>();
        attributesMap.put("phoneNumber", Collections.singletonList(user.getPhone()));
        attributesMap.put("cardIdNumber", Collections.singletonList(user.getCardidnumber()));
        attributesMap.put("address", Collections.singletonList(composeAddressString(user)));
        return attributesMap;
    }

    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        UserRepresentation user = findUserByEmail(passwordResetRequest.getEmail())
                .orElseThrow(() -> new UserNotExistException("User not found"));

        updateUserPassword(user, passwordResetRequest.getNewPassword());
    }

    // Method to find user by email
    private Optional<UserRepresentation> findUserByEmail(String email) {
        List<UserRepresentation> users = keycloak.realm("SecurityManager").users().search(email);
        return users.stream().findFirst();
    }

    // Method to update user's password
    private void updateUserPassword(UserRepresentation user, String newPassword) {
        user.setCredentials(List.of(createPasswordCredential(newPassword)));
        keycloak.realm("SecurityManager").users().get(user.getId()).update(user);
    }

    // Helper method to create a password credential
    private CredentialRepresentation createPasswordCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return credential;
    }
    public List<UserSummary> getAllUsers() {
        List<UserRepresentation> users = keycloak.realm("SecurityManager").users().list();
        List<UserEntity> userEntities=this.userRepository.findAll();
        return userEntities.stream().map(this::toUserSummary).collect(Collectors.toList());
    }
    private UserSummary toUserSummary(UserEntity user) {
        UserSummary userSummary = new UserSummary();
        userSummary.setUsername(user.getUsername());
        userSummary.setFirstName(user.getFirstName());
        userSummary.setLastName(user.getLastName());
        userSummary.setEmail(user.getEmail());

        // Assuming you have methods to get phone number and ID card number
        // These may need to be adjusted based on your actual implementation
        userSummary.setPhone(user.getPhone());
        return userSummary;

    }

    public UserSummaryWithoutSensitiveData getUserByEmail(String email) {
UserEntity user= userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotExistException("User not found with email: " + email));


  return mapUserRepresentationToUser(user);

    }
    public UserSummaryWithoutSensitiveData mapUserRepresentationToUser(UserEntity userRepresentation) {
        if (userRepresentation == null) {
          throw new UserNotExistException("User Not found");
        }

        UserSummaryWithoutSensitiveData user = new UserSummaryWithoutSensitiveData();
       // user.setId(userRepresentation.getId());
        user.setUsername(userRepresentation.getUsername());
        user.setFirstName(userRepresentation.getFirstName());
        user.setLastName(userRepresentation.getLastName());
        user.setEmail(userRepresentation.getEmail());
        user.setPhone(userRepresentation.getPhone());
        user.setCardidnumber(userRepresentation.getCardidnumber());
        Address address = new Address();
        address.setCity(userRepresentation.getAddress().getCity());
        address.setStreet(userRepresentation.getAddress().getStreet());
        address.setPostalCode(userRepresentation.getAddress().getPostalCode());
        address.setCountry(userRepresentation.getAddress().getCountry());
        user.setAddress(address);
        return user;
    }

    public void updateUser(String userId, String tokenSubId, User updatedUser) {
        // Validation du tokenSubId
        if (tokenSubId == null || tokenSubId.isEmpty()) {
            throw new IllegalArgumentException("TokenSubId must be provided");
        }

        // Mise à jour dans la base de données
        updateUserInDatabase(userId, updatedUser);

        // Mise à jour dans Keycloak
        updateUserInKeycloak(tokenSubId, updatedUser);
    }

    private void updateUserInDatabase(String userId, User updatedUser) {
        UserEntity existingUserEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException("User not found with ID: " + userId));

        // Mettre à jour les informations de l'utilisateur
        existingUserEntity.setUsername(updatedUser.getUsername());
        existingUserEntity.setFirstName(updatedUser.getFirstName());
        existingUserEntity.setLastName(updatedUser.getLastName());
        existingUserEntity.setEmail(updatedUser.getEmail());
        existingUserEntity.setPhone(updatedUser.getPhone());
        existingUserEntity.setCardidnumber(updatedUser.getCardidnumber());

        // Mettre à jour l'adresse si elle est fournie
        if (updatedUser.getAddress() != null) {
            UserEntity.Address address = existingUserEntity.getAddress();
            if (address == null) {
                address = new UserEntity.Address();
            }
            address.setCity(updatedUser.getAddress().getCity());
            address.setStreet(updatedUser.getAddress().getStreet());
            address.setPostalCode(updatedUser.getAddress().getPostalCode());
            address.setCountry(updatedUser.getAddress().getCountry());
            existingUserEntity.setAddress(address);
        }

        try {
            userRepository.save(existingUserEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new UserAlreadyExistException(ex.getMessage());
        }
    }

    private void updateUserInKeycloak(String userId, User updatedUser) {
        UserRepresentation userRepresentation = convertToUserRepresentation(updatedUser);

        // Récupérer l'utilisateur dans Keycloak
        UserRepresentation existingUser = keycloak.realm("SecurityManager").users().get(userId.toString()).toRepresentation();
        if (existingUser == null) {
            throw new UserNotExistException("User not found in Keycloak with ID: " + userId);
        }

        existingUser.setFirstName(userRepresentation.getFirstName());
        existingUser.setLastName(userRepresentation.getLastName());
        existingUser.setEmail(userRepresentation.getEmail());
        existingUser.setAttributes(userRepresentation.getAttributes());


        // Utiliser tokenSubId si nécessaire pour la validation ou le logging
        System.out.println("Updating user with ID: " + userId + " using tokenSubId: " + userId);

        keycloak.realm("SecurityManager").users().get(userId.toString()).update(existingUser);
    }



}
