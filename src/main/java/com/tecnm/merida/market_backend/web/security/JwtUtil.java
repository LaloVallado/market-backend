package com.tecnm.merida.market_backend.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// Clase utilitaria: construye el token JWT y extrae su informacion validando la firma
@Component
public class JwtUtil {

    // Clave secreta para HS256: debe tener al menos 32 caracteres (esta tiene 47)
    private static final String SECRET_KEY = "market-backend-tecnm-merida-jwt-secret-key-2026";

    // El token dura 1 hora
    private static final long EXPIRATION_MS = 1000 * 60 * 60;

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    // Construye el token con el correo del cliente como "subject"
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    // Extrae el correo del token (valida la firma al leerlo)
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // true si la firma es correcta y el token no ha expirado
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}