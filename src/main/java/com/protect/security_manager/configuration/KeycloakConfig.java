
package com.protect.security_manager.configuration;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class KeycloakConfig {


    @Value("${keycloak.serverUrl}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${keycloak.clientSecret}")
    private String clientSecret;

    @Value("${keycloak.username}")
    private String userName;

    @Value("${keycloak.password}")
    private String password;
    @Bean
    public  Keycloak buildKeycloakInstance(){


       return  KeycloakBuilder.builder() //
               .serverUrl(serverUrl) //
               .realm(realm) //
               .grantType(OAuth2Constants.CLIENT_CREDENTIALS) //
               .clientId(clientId) //
               .clientSecret(clientSecret) //
               .username(userName) //
               .password(password) //
               .build();
    }


}
