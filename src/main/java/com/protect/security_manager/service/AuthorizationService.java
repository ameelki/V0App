package com.protect.security_manager.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.protect.security_manager.util.JwtUtil;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorizationService {

    private final JwtUtil jwtUtil;

    public AuthorizationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    public boolean hasRole (String token, String roleToCheck) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String verifiedToken = token.replace("Bearer ", "");
            DecodedJWT jwt = jwtUtil.decodeToken(verifiedToken);
            Map<String, Claim> claims = jwt.getClaims();
            // Vérifie et affiche si le rôle existe dans les rôles de realm
            boolean hasRealmRole = processRealmRoles(claims, roleToCheck);

            // Vérifie et affiche si le rôle existe dans les rôles de client
            boolean hasClientRole = processClientRoles(claims, roleToCheck);

            return hasRealmRole || hasClientRole;
        }
    public boolean hasRoleAndCheckResourcesForSameConnectedPerson(String token, String roleToCheck, String personId) {
        // Récupère l'objet d'authentification actuel
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Supprime "Bearer " du token
        String verifiedToken = token.replace("Bearer ", "");

        // Décode le token pour obtenir les revendications (claims)
        DecodedJWT jwt = jwtUtil.decodeToken(verifiedToken);
        Map<String, Claim> claims = jwt.getClaims();

        // Vérifie si le 'sub' du token correspond au 'personId' passé en paramètre
        String tokenSub = claims.get("sub").asString();
        if (!tokenSub.equals(personId)) {
            // Retourne 'false' si le 'sub' du token ne correspond pas
            return false;
        }

        // Vérifie et affiche si le rôle existe dans les rôles de realm
        boolean hasRealmRole = processRealmRoles(claims, roleToCheck);

        // Vérifie et affiche si le rôle existe dans les rôles de client
        boolean hasClientRole = processClientRoles(claims, roleToCheck);

        // Retourne 'true' si l'utilisateur a le rôle requis et que le 'sub' correspond
        return hasRealmRole || hasClientRole;
    }



    private boolean processRealmRoles(Map<String, Claim> claims, String roleToCheck) {
        return claims.containsKey("realm_access") &&
                ((List<String>) claims.get("realm_access").asMap().get("roles")).stream()
                        .anyMatch(role -> role.equals(roleToCheck));
    }

    private boolean processClientRoles(Map<String, Claim> claims, String roleToCheck) {
        Claim resourceAccessClaim = claims.get("resource_access");
        if (resourceAccessClaim != null) {
            Map<String, Object> resourceAccess = resourceAccessClaim.asMap();
            return resourceAccess.entrySet().stream()
                    .map(entry -> (Map<String, Object>) entry.getValue())
                    .map(clientAccess -> (List<String>) clientAccess.get("roles"))
                    .anyMatch(clientRoles -> clientRoles.contains(roleToCheck));
        } else {
            return false;
        }

    }
}

