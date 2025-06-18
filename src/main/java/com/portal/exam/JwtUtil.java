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

/**
 * Utility class for JWT (JSON Web Token) operations:
 * - Generating new JWT tokens
 * - Extracting information (username, expiration) from tokens
 * - Validating tokens
 *
 * This class uses the JJWT API (0.11.x) and thus uses methods
 * that were later deprecated in 0.12.x.
 */
@Service
public class JwtUtil {

    // IMPORTANT: Keep your secret key secure!
    private String SECRET_KEY = "yourverysecretkeythatisatleast256bitslongandveryrandom";

    /**
     * Extracts the username (subject) from the given JWT token.
     * @param token The JWT token string.
     * @return The username.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the given JWT token.
     * @param token The JWT token string.
     * @return The expiration date.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token using a claims resolver function.
     * @param token The JWT token string.
     * @param claimsResolver A function to resolve a specific claim from the Claims object.
     * @param <T> The type of the claim to extract.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the JWT token and extracts all its claims.
     * Uses the Jwts.parser() API (compatible with 0.11.x).
     *
     * @param token The JWT token string.
     * @return The Claims (payload) of the token.
     */
    private Claims extractAllClaims(String token) {
        // For JJWT 0.11.x, you use Jwts.parser().setSigningKey(SECRET_KEY)
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the JWT token is expired.
     * @param token The JWT token string.
     * @return True if the token is expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a new JWT token for the given UserDetails.
     * @param userDetails The UserDetails object containing user information.
     * @return The generated JWT token string.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Creates the JWT token string with claims, subject, issue date, expiration, and signature.
     * Uses the Jwts.builder() API (compatible with 0.11.x).
     *
     * @param claims Custom claims to include in the token.
     * @param subject The subject (username) of the token.
     * @return The compact JWT token string.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Set expiration time (10 hours from now)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Sign with the secret key string
                .compact();
    }

    /**
     * Validates the given JWT token against the provided UserDetails.
     * Checks if the username matches and if the token is not expired.
     *
     * @param token The JWT token string.
     * @param userDetails The UserDetails object to validate against.
     * @return True if the token is valid, false otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
