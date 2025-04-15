package com.eduardo.paytracker.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.eduardo.paytracker.exception.TokenException;
import com.eduardo.paytracker.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user){
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("PayTracker API")
                    .withSubject(user.getEmail())
                    .withExpiresAt(tokenExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException e){
            throw new RuntimeException("Token generation error ", e);
        }
    }

    public String validateToken(String token){
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("PayTracker API")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new TokenException("Invalid or expired token!");
        }
    }

    private Instant tokenExpiration(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
