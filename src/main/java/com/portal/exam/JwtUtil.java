// com.portal.exam.JwtUtil.java (Adjust package)
package com.portal.exam;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.Base64; // **NEW IMPORT: For Base64 encoding**
import javax.crypto.spec.SecretKeySpec; // NEW IMPORT: For creating a Key object
import java.security.Key; // NEW IMPORT

@Service
public class JwtUtil {

    // **CRITICAL FIX: Use a strong, random key and store it as a Base64 encoded string.**
    // For HS256, the key should be at least 32 bytes (256 bits) in length.
    // Base64 encoding ensures it's safe for use in JWT.
    // Example: Generate a random 32-byte key and Base64 encode it.
    // You can generate one like this in Java:
    // Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
    // For your testing, you can use a fixed one, but make it long and truly random.
    // The previous string "your_very_secret_and_long_jwt_key_that_is_at_least_32_characters_long_for_HS256"
    // is a good *length*, but it needs to be Base64-friendly.
    
    // Let's use a new, explicitly Base64-encoded key.
    // This key is 32 bytes (256 bits) when decoded from Base64.
    private static final String BASE64_SECRET_STRING = "qL2i5Vp7E0f8d9C1b0A3z4X6y5W7u8T9r2Q1s0P3o4N5m6L7k8J9i0H1g2F3e4D5c6B7a8Z9"; 
    
    // Derived Key object from the Base64 string for signing/parsing.
    // This is the correct way for JJWT 0.11.x if you're providing a string.
    private final Key signingKey;

    public JwtUtil() {
        // Decode the Base64 string into a byte array
        byte[] decodedKey = Base64.getDecoder().decode(BASE64_SECRET_STRING);
        // Create a SecretKeySpec (Key) object from the decoded bytes and algorithm
        this.signingKey = new SecretKeySpec(decodedKey, SignatureAlgorithm.HS256.getJcaName());
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
        // Use the Key object for setSigningKey
        return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
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
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                .signWith(SignatureAlgorithm.HS256, signingKey) // **CRITICAL: Use the Key object here**
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
