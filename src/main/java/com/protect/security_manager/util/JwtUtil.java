package com.protect.security_manager.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    public  List<String> getRolesFromToken(String token) {
        DecodedJWT jwt = decodeToken(token);
        return jwt.getClaim("realm_access").asList(String.class);
    }
    public DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }

}
