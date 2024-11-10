package com.protect.security_manager.service;
import com.protect.security_manager.entity.AddsImageEntity;
import com.protect.security_manager.entity.PersonImageEntity;
import com.protect.security_manager.entity.UserEntity;
import com.protect.security_manager.exception.ApplicationException;
import com.protect.security_manager.exception.InvalidUserCredentiel;
import com.protect.security_manager.exception.ResourceNotFoundException;
import com.protect.security_manager.exception.UserNotExistException;
import com.protect.security_manager.repository.AddsImageEntityRepository;
import com.protect.security_manager.repository.PersonImageEntityRepository;
import com.protect.security_manager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import security.manager.model.ImageManagerGetPictureImageTypeGet200Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;
@Service
public class ImageService {

    @Value("${image.upload.path}")
    private String uploadPath;

    @Autowired
    private PersonImageEntityRepository personImageEntityRepository;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private AddsImageEntityRepository addsImageEntityRepository;
    @Transactional
    public String saveImage(String base64Image, String imageType, Optional<String> personId, Optional<Integer> addsId, String fileName) throws IOException {
        // Vérifications pour le type d'image et les identifiants
        if ("PERSON".equalsIgnoreCase(imageType) && personId.isEmpty()) {
            throw new IllegalArgumentException("personId is required for PERSON image type");
        } else if ("ADDS".equalsIgnoreCase(imageType) && addsId.isEmpty()) {
            throw new IllegalArgumentException("addsId is required for ADDS image type");
        }

        // Vérification si l'utilisateur existe (pour le type "PERSON")
        if ("PERSON".equalsIgnoreCase(imageType) && personId.isPresent()) {
            UserEntity user = userRepository.findById(personId.get())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + personId.get()));
        }

        // Extraire l'extension du fichier à partir du nom de fichier fourni
        String extension = getFileExtension(fileName);
        if (extension == null) {
            throw new IllegalArgumentException("Invalid image format.");
        }

        // Décoder l'image depuis le base64
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        // Nom du dossier principal "PERSON_NAS_PHOTO"
        String mainFolder = "PERSON_NAS_PHOTO";
        File mainDir = new File(uploadPath + File.separator + mainFolder);
        if (!mainDir.exists()) {
            mainDir.mkdirs();  // Crée le dossier principal s'il n'existe pas
        }

        // Créer le sous-dossier spécifique à chaque personne ou ajout
        String folderName = imageType.equals("PERSON") ? "person_" + personId.get() : "adds_" + addsId.get();
        File subDir = new File(mainDir, folderName);
        if (!subDir.exists()) {
            subDir.mkdirs();  // Crée le sous-dossier si nécessaire
        }

        // Reformater le nom du fichier
        String reformatedfileName = (personId.isPresent() ? personId.get() : addsId.get()) + "." + extension;

        // Enregistrer l'image dans le sous-dossier
        Path path = Paths.get(subDir.getAbsolutePath(), reformatedfileName);

        if (Files.exists(path)) {
            throw  new InvalidUserCredentiel("The user already has an image with this name");

        }

        Files.write(path, imageBytes);

        // Sauvegarder les métadonnées de l'image (nom du fichier, URL, etc.)
        String imageUrl = path.toString();
        saveImageMetadata(imageType, personId.orElse(""), addsId.orElse(null), imageUrl, reformatedfileName);

        return "Image saved successfully";
    }




    public String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("Invalid file name or no extension found.");
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1); // Extrait l'extension
    }

    private void saveImageMetadata(String imageType, String personId, Integer addsId, String imageUrl,String fileName ) {
        if ("PERSON".equalsIgnoreCase(imageType)) {
            PersonImageEntity personImage = new PersonImageEntity();
            personImage.setPersonId(personId);
            personImage.setOwnerId(personId);
            personImage.setUrl(imageUrl);
            personImage.setFileName(fileName);
            personImage.setUploadDate(LocalDateTime.now());

            personImageEntityRepository.save(personImage);
        } else if ("ADDS".equalsIgnoreCase(imageType)) {
            AddsImageEntity addsImage = new AddsImageEntity();
            addsImage.setAddsId(Integer.valueOf(addsId.toString()));
            addsImage.setUrl(imageUrl);
            addsImageEntityRepository.save(addsImage);
        }
    }






    public ImageManagerGetPictureImageTypeGet200Response getImageBase64ByPersonId(String personId) {

        Optional<PersonImageEntity> personImageEntityOptional = getImageByPersonId(personId);

        if (personImageEntityOptional.isPresent()) {
            // Récupérer le chemin de l'image et le nom du fichier
            String imagePath = personImageEntityOptional.get().getUrl();  // Assurez-vous que 'getUrl()' donne le chemin complet
            String fileName = personImageEntityOptional.get().getFileName();  // Vous devriez stocker ce nom en base de données
            Path path = Paths.get(imagePath);
            try {
                // Lire les octets de l'image
                byte[] imageBytes = Files.readAllBytes(path);
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                // Créer l'objet de réponse
                ImageManagerGetPictureImageTypeGet200Response response = new ImageManagerGetPictureImageTypeGet200Response();
                response.setImage(base64Image);  // Ajouter l'image encodée en base64
                response.setFileName(fileName);  // Ajouter le nom du fichier

                return response;
            } catch (IOException e) {
            throw new ApplicationException("An unexpected error occurred. Please consult the support team with this error.");
            }
        } else {
            return null; // Aucun fichier trouvé, vous pouvez aussi retourner une erreur
        }
    }
    private Optional<PersonImageEntity> getImageByPersonId(String personId) {
        // Méthode pour récupérer l'entité Image par 'personId'
        return Optional.ofNullable(personImageEntityRepository.findByOwnerId(personId)
                .orElseThrow(() -> new UserNotExistException("User with ID " + personId + " does not exist")));
    }

}