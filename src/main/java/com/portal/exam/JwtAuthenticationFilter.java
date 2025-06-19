// com.portal.exam.JwtAuthenticationFilter.java (Adjust package)
package com.portal.exam;

import com.portal.exam.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        System.out.println("DEBUG: Authorization Header received: " + (requestTokenHeader != null ? requestTokenHeader : "null"));

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7); // Extract token (after "Bearer ")
            System.out.println("DEBUG: Extracted JWT Token: " + jwtToken);
            
            try {
                username = jwtUtil.extractUsername(jwtToken);
                System.out.println("DEBUG: Extracted Username: " + username);
            } catch (ExpiredJwtException e) {
                System.out.println("ERROR: JWT Token has expired for user: " + username + " - " + e.getMessage());
            } catch (MalformedJwtException e) {
                System.out.println("ERROR: Invalid JWT Token structure (MalformedJwtException): " + e.getMessage());
            } catch (SignatureException e) {
                System.out.println("ERROR: Invalid JWT signature (SignatureException): " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: Unable to get JWT Token or illegal argument (IllegalArgumentException): " + e.getMessage());
            } catch (Exception e) {
                System.out.println("ERROR: General JWT error during extraction: " + e.getClass().getName() + " - " + e.getMessage());
                e.printStackTrace(); // Print full stack trace for unexpected errors
            }

        } else {
            System.out.println("DEBUG: Invalid JWT Token format: Header is null or does not start with 'Bearer '.");
        }

        // Validate the token and set authentication context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("DEBUG: Username found, SecurityContext is empty. Attempting authentication.");
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("INFO: Authentication successful for user: " + username + " - SecurityContext updated.");

            } else {
                System.out.println("WARN: JWT Token validation failed for user: " + username);
            }
        } else if (username != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            System.out.println("DEBUG: Authentication already exists in SecurityContext for user: " + username);
        } else {
            // This path is taken if username is null (token invalid/missing) AND SecurityContext is empty.
            System.out.println("DEBUG: No valid username from token, or SecurityContext already populated for other reasons.");
        }

        filterChain.doFilter(request, response);
    }
}
