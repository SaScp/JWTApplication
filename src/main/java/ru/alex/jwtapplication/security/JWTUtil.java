package ru.alex.jwtapplication.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    public String generateToken(String username){
        Date date = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        return JWT.create()
                .withSubject("user")
                .withClaim("username",username)
                .withIssuedAt(new Date())
                .withIssuer("alex")
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256("AlexJWT"));
    }

    public String convertJWT(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("AlexJWT"))
                .withSubject("user")
                .withIssuer("alex")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
