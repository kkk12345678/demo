package com.example.demo.util;

import io.jsonwebtoken.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.lang.System.currentTimeMillis;

@Component
public class JwtTokenUtil {
    public static final int ACCESS_TOKEN = 1;
    public static final int REFRESH_TOKEN = 2;

    private final String accessTokenSecretKey;
    private final String refreshTokenSecretKey;

    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;

    public JwtTokenUtil(Environment env) {
        this.accessTokenSecretKey = Objects.requireNonNull(env.getProperty("jwt.access.secret.key"));
        this.refreshTokenSecretKey = Objects.requireNonNull(env.getProperty("jwt.refresh.secret.key"));
        this.accessTokenExpiration = Long.parseLong(Objects.requireNonNull(env.getProperty("jwt.access.expiration.ms")));
        this.refreshTokenExpiration = Long.parseLong(Objects.requireNonNull(env.getProperty("jwt.refresh.expiration.ms")));
    }

    public Map<Integer, String> generateToken(String name, String email, String role) {
        HashMap<Integer, String> tokens = new HashMap<>(2);
        tokens.put(ACCESS_TOKEN, Jwts.builder()
                .claim("name", name)
                .claim("email", email)
                .claim("role", role)
                .setExpiration(new Date(currentTimeMillis() + accessTokenExpiration))
                .signWith(SignatureAlgorithm.HS512, accessTokenSecretKey)
                .compact());
        tokens.put(REFRESH_TOKEN, Jwts.builder()
                .claim("name", name)
                .claim("email", email)
                .claim("role", role)
                .setExpiration(new Date(currentTimeMillis() + refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS512, refreshTokenSecretKey)
                .compact());
        return tokens;
    }

    public Claims validateAccessToken(String accessToken) {
        return Jwts.parser().setSigningKey(accessTokenSecretKey).parseClaimsJws(accessToken).getBody();
    }

    public Claims validateRefreshToken(String refreshToken) {
        return Jwts.parser().setSigningKey(refreshTokenSecretKey).parseClaimsJws(refreshToken).getBody();
    }
}
