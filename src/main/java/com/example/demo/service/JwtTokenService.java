package com.example.demo.service;

import com.example.demo.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.lang.System.currentTimeMillis;

@Service
public class JwtTokenService {
    public static final int ACCESS_TOKEN = 1;
    public static final int REFRESH_TOKEN = 2;

    private final String accessTokenSecretKey;
    private final String refreshTokenSecretKey;

    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;

    public JwtTokenService(Environment env) {
        this.accessTokenSecretKey = Objects.requireNonNull(env.getProperty("jwt.access.secret.key"));
        this.refreshTokenSecretKey = Objects.requireNonNull(env.getProperty("jwt.refresh.secret.key"));
        this.accessTokenExpiration = Long.parseLong(Objects.requireNonNull(env.getProperty("jwt.access.expiration.ms")));
        this.refreshTokenExpiration = Long.parseLong(Objects.requireNonNull(env.getProperty("jwt.refresh.expiration.ms")));
    }

    public Map<Integer, String> generateToken(String name, String email, String role) {
        HashMap<Integer, String> tokens = new HashMap<>(2);
        tokens.put(ACCESS_TOKEN, Jwts.builder().setSubject(name)
                .setExpiration(new Date(currentTimeMillis() + accessTokenExpiration))
                .setIssuer(email)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS512, accessTokenSecretKey).compact());
        tokens.put(REFRESH_TOKEN, Jwts.builder().setSubject(name)
                .setExpiration(new Date(currentTimeMillis() + refreshTokenExpiration))
                .setIssuer(email)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS512, refreshTokenSecretKey).compact());
        return tokens;
    }

    public String getStructure(String token, int tokenType) throws ValidationException {
        if (tokenType == ACCESS_TOKEN) {
            Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(accessTokenSecretKey).parseClaimsJws(token);
            return "Header     : " + parseClaimsJws.getHeader() + "\n" +
                   "Body       : " + parseClaimsJws.getBody() + "\n" +
                   "Signature  : " + parseClaimsJws.getSignature();
        } else if (tokenType == REFRESH_TOKEN) {
            Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(refreshTokenSecretKey).parseClaimsJws(token);
            return "Header     : " + parseClaimsJws.getHeader() + "\n" +
                   "Body       : " + parseClaimsJws.getBody() + "\n" +
                   "Signature  : " + parseClaimsJws.getSignature();
        } else throw new ValidationException("Token is invalid.");
    }
}
