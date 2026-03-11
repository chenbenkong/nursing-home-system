package com.nursinghome.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类 - Spring Boot 3.x 兼容版本
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret:nursing_home_secret_key_2024_for_hmac_sha256}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    private static String STATIC_SECRET_KEY;
    private static long STATIC_EXPIRATION;
    private static SecretKey KEY;

    @PostConstruct
    public void init() {
        STATIC_SECRET_KEY = secretKey;
        STATIC_EXPIRATION = expiration;
        // 使用 Keys.hmacShaKeyFor 创建安全的密钥（需要至少32字节的密钥）
        byte[] keyBytes = STATIC_SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        // 如果密钥长度不足32字节，进行填充
        if (keyBytes.length < 32) {
            byte[] paddedKey = new byte[32];
            System.arraycopy(keyBytes, 0, paddedKey, 0, keyBytes.length);
            keyBytes = paddedKey;
        }
        KEY = Keys.hmacShaKeyFor(keyBytes);
    }

    public static String generateToken(Long userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + STATIC_EXPIRATION);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.valueOf(claims.get("userId").toString());
    }

    public static String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username").toString();
    }

    public static String getRoleFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("role").toString();
    }
}
