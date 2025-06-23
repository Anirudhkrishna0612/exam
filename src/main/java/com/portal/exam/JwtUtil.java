// src/main/java/com/portal/exam/JwtUtil.java
package com.portal.exam; // Adjust package as per your project structure

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys; // Import for secure key generation from string
import org.springframework.beans.factory.annotation.Value; // For injecting properties
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey; // For the SecretKey object
import java.nio.charset.StandardCharsets; // For converting string to bytes
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    // **CRITICAL FIX: Inject the secret from application.properties**
    // Ensure jwt.secret in application.properties is set to "examportalbylearncodeonline"
    @Value("${jwt.secret}")
    private String SECRET_KEY_STRING; // This will hold "examportalbylearncodeonline"

    private SecretKey signingKey; // Declare the SecretKey

    // Constructor to initialize the signingKey from the injected string
    public JwtUtil() {
        // The @Value annotation injects the value AFTER the constructor is called
        // So, we need to initialize signingKey in a method or during the first access.
        // A better approach is to make JwtUtil a @ConfigurationProperties or use @PostConstruct
    }

    // This method will ensure the signingKey is initialized from the injected SECRET_KEY_STRING
    private SecretKey getSigningKey() {
        if (this.signingKey == null) {
            // **CRITICAL: Generate a secure SecretKey from your string secret.**
            // Keys.hmacShaKeyFor expects bytes. Your "examportalbylearncodeonline" is 26 bytes.
            // For HS256, a key of at least 32 bytes (256 bits) is recommended.
            // If the string is shorter, JJWT might automatically pad or throw a warning.
            // Using StandardCharsets.UTF_8 is good practice.
            this.signingKey = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8));
        }
        return this.signingKey;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // **CRITICAL FIX: Use modern JJWT parserBuilder()**
        return Jwts.parserBuilder()
                   .setSigningKey(getSigningKey()) // Use the securely derived SecretKey
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Set expiration (e.g., 10 hours from now)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                // **CRITICAL FIX: Use modern JJWT signWith(key, algorithm)**
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Use the securely derived SecretKey
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
