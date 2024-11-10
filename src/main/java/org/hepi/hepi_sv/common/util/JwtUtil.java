package org.hepi.hepi_sv.common.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    private static final Key ACCESS_TOKEN_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final Key REFRESH_TOKEN_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final long ACCESS_TOKEN_EXPIRATION = 60 * 60 * 1000; // 1시간
    private static final long REFRESH_TOKEN_EXPIRATION = 30 * 24 * 60 * 60 * 1000; // 30일

    public static String generateAccessToken(UUID userUuid) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userUuid", userUuid.toString());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userUuid.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(ACCESS_TOKEN_SECRET_KEY)
                .compact();
    }

    public static String generateRefreshToken(UUID userUuid) {
        return Jwts.builder()
                .setSubject(userUuid.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(REFRESH_TOKEN_SECRET_KEY)
                .compact();
    }

    public static boolean validateToken(String token, boolean isAccessToken) {
        try {
            Key key = isAccessToken ? ACCESS_TOKEN_SECRET_KEY : REFRESH_TOKEN_SECRET_KEY;
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static UUID extractUserUuid(String token, boolean isAccessToken) {
        Key key = isAccessToken ? ACCESS_TOKEN_SECRET_KEY : REFRESH_TOKEN_SECRET_KEY;
        String uuidString = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        return UUID.fromString(uuidString); 
    }
}
