package com.sergioruy.supportportal.utility;

import com.auth0.jwt.JWT;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.sergioruy.supportportal.constant.SecurityConstant.*;
import com.sergioruy.supportportal.domain.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class JWTTokenProvider {
    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtToken(UserPrincipal userPrincipal) {
        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create().withIssuer(SERGIO_RUY_DEV).withAudience(SERGIO_RUY_ADMIN)
                .withIssuedAt(new Date()).withSubject(userPrincipal.getUsername())
                .withArrayClaim(AUTHORITIES, claims).withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(secret.getBytes()));
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
    }

}
