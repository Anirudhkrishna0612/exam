package com.portal.exam; // Ensure this package matches your file location

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * Custom JWT Authentication Filter that extends OncePerRequestFilter.
 * This filter intercepts incoming requests to validate JWT tokens and set up
 * Spring Security's authentication context.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserServiceImpl userDetailsService; // Your custom UserDetailsService implementation

    @Autowired
    private JwtUtil jwtUtil; // Your JWT utility class

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Get the Authorization header from the request
        final String requestTokenHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + requestTokenHeader); // Log the header for debugging

        String username = null;
        String jwtToken = null;

        // Check if the header is not null and starts with "Bearer "
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7); // Extract the token (after "Bearer ")
            
            try {
                // Extract username from the token using JwtUtil
                username = this.jwtUtil.extractUsername(jwtToken);
            } catch (ExpiredJwtException e) {
                // Handle expired token exception
                System.out.println("JWT Token has expired for user: " + username + ". Error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                // Handle invalid token arguments
                System.out.println("Unable to get JWT Token or Invalid JWT Token. Error: " + e.getMessage());
            } catch (Exception e) {
                // Catch any other general JWT parsing exceptions
                e.printStackTrace();
                System.out.println("JWT Token validation error: " + e.getMessage());
            }

        } else {
            System.out.println("Invalid JWT Token: Token does not begin with Bearer String or is null.");
            // Important: Don't just return here. Let the filter chain continue.
            // If the token is missing or malformed, Spring Security's
            // ExceptionTranslationFilter (and your JwtAuthenticationEntryPoint)
            // will handle unauthorized access downstream.
        }

        // Validate the token and set authentication in SecurityContextHolder if needed
        // Only proceed if username is found and no authentication is currently set in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load UserDetails by username
            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate the token against UserDetails
            if (this.jwtUtil.validateToken(jwtToken, userDetails)) {
                // If token is valid, create an Authentication object
                // Pass userDetails, credentials (null for JWT), and authorities
                UsernamePasswordAuthenticationToken usernamePasswordAuthentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Set details from the request (e.g., remote address, session ID)
                usernamePasswordAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // **CRITICAL FIX: Set the authentication object, NOT null!**
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthentication);
                System.out.println("Authentication successful for user: " + username);

            } else {
                System.out.println("JWT Token is not valid for user: " + username);
            }
        } else if (username != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            // This case happens if authentication was already set by a previous filter (e.g., for a forwarded request)
            System.out.println("Authentication already exists in SecurityContext for user: " + username);
        }

        // Continue the filter chain. This passes the request to the next filter in Spring Security,
        // which will then use the authentication set in SecurityContextHolder (if any).
        filterChain.doFilter(request, response);
    }
}
