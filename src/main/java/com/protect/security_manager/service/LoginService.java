package com.protect.security_manager.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.protect.security_manager.exception.ErrorMessage;
import com.protect.security_manager.exception.InvalidUserCredentiel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import security.manager.model.AccessTokenAuthorization;
import security.manager.model.LoginFormRequest;

@Service
public class LoginService {

    @Autowired
    private RestTemplate restTemplate;


   @Value("${keycloak.token.url}")
    private String keycloakTokenUrl;

    @Value("${keycloak.client.id}")
    private String keycloakClientId;

    @Value("${keycloak.client.secret}")
    private String keycloakClientSecret;

    @Value("${keycloak.scope}")
    private String keycloakScope;
    @Value("${keycloak.client.grant}")
    private String grantType;





     public AccessTokenAuthorization getToken(LoginFormRequest loginFormRequest
    ) {


         AccessTokenAuthorization accessTokenAuthorization=new AccessTokenAuthorization();

        HttpHeaders headers = buildHttpHeaders();
        String requestBody = buildRequestBody(keycloakClientId, keycloakClientSecret, keycloakScope, loginFormRequest.getUsername(), loginFormRequest.getPassword());

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    keycloakTokenUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody, headers),
                    String.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String responseBody = responseEntity.getBody();
                String accessToken = parseAccessToken(responseBody);
                // Méthode à implémenter
                accessTokenAuthorization.setAccessToken(accessToken);
                accessTokenAuthorization.setTokenType("access_token");
                buildAccessTokenAuthorization(accessTokenAuthorization, accessToken);
            } else {
                throw new InvalidUserCredentiel(ErrorMessage.INVALID_CREDENETIEL);

            }
        } catch (Exception e) {

            throw new InvalidUserCredentiel(ErrorMessage.INVALID_CREDENETIEL);
        }

        return accessTokenAuthorization;
    }

    private HttpHeaders buildHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Cookie", "JSESSIONID=72BBA56810415EFC46C7E26EBEF9D9D3");
        return headers;
    }

    private String buildRequestBody(String clientId, String clientSecret, String scope, String username, String password) {
        return "client_id=" + clientId +
                "&grant_type=password" +
                "&client_secret=" + clientSecret +
                "&scope=" + scope +
                "&username=" + username +
                "&password=" + password;
    }

    private String parseAccessToken(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(responseBody);
            return rootNode.path("access_token").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void buildAccessTokenAuthorization(AccessTokenAuthorization accessTokenAuthorization, String accessToken) {
        accessTokenAuthorization.setAccessToken(accessToken);
        accessTokenAuthorization.setTokenType("Bearer"); // Typiquement "Bearer" pour un token JWT
    }



}

