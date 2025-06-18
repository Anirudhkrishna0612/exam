package com.portal.exam; // Ensure this package matches your file location

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // NEW IMPORT: Required for HttpMethod.OPTIONS
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // NEW IMPORT: Required for SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Needed for addFilterBefore (future)

/**
 * Main Spring Security configuration for the application.
 * - @Configuration: Marks this class as a source of bean definitions.
 * - @EnableWebSecurity: Enables Spring Security's web security features.
 * - @EnableGlobalMethodSecurity: Enables method-level security (e.g., @PreAuthorize).
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // This is good if you plan to use @PreAuthorize/@PostAuthorize
public class SecurityConfig {

    // Autowire your custom JWT Authentication Entry Point.
    // This will be invoked when an unauthenticated user tries to access a protected resource.
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler; // Ensure this class is created and @Component

    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter; 

    /**
     * Configures and exposes the AuthenticationManager bean.
     * This manager is responsible for authenticating users (e.g., in your login endpoint).
     *
     * @param authenticationConfiguration Provides access to the shared AuthenticationConfiguration.
     * @return The configured AuthenticationManager.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures the global AuthenticationManagerBuilder.
     * This tells Spring Security to use your custom UserDetailsService
     * and the configured PasswordEncoder for authentication.
     *
     * @param auth The AuthenticationManagerBuilder to configure.
     * @param userDetailsService The UserDetailsService bean (your UserServiceImpl).
     * @param passwordEncoder The PasswordEncoder bean (from PasswordEncoderConfig).
     * @throws Exception If an error occurs during configuration.
     */
    @Autowired // Method injection
    public void configureGlobal(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * Configures the security filter chain.
     * This defines the rules for HTTP requests (who can access what) and sets up JWT-specific behaviors.
     *
     * @param http The HttpSecurity object to configure.
     * @return The built SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (Cross-Site Request Forgery) protection for stateless APIs.
            .csrf(csrf -> csrf.disable())
            // Disable Spring Security's default CORS handling.
            // You should have a separate CORS configuration (e.g., a @Bean CorsConfigurationSource,
            // or @CrossOrigin on controllers) if your frontend is on a different origin.
            .cors(cors -> cors.disable()) // ADDED: Disables Spring Security's CORS config

            // Configure authorization rules for HTTP requests.
            .authorizeHttpRequests(authorize -> authorize
                // Allow unauthenticated access to the user signup and token generation (login) endpoints.
                .antMatchers("/user/", "/generate-token").permitAll() // ADDED: /generate-token for login endpoint
                // Allow OPTIONS requests (preflight requests from browsers) for all paths.
                // This is crucial for CORS to work correctly.
                .antMatchers(HttpMethod.OPTIONS).permitAll() // ADDED: Permitting OPTIONS method
                // All other requests require authentication.
                .anyRequest().authenticated()
            )
            // Configure exception handling for unauthorized access.
            // When an unauthenticated user tries to access a protected resource,
            // the JwtAuthenticationEntryPoint will be invoked.
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(unauthorizedHandler) // ADDED: Custom entry point
            )
            // Configure session management to be stateless.
            // This is vital for JWT-based authentication as the server does not maintain sessions.
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // ADDED: Stateless session policy
            )
            // Disable default form login and HTTP Basic authentication as we are using JWT.
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> httpBasic.disable());

        // You will add your custom JWT Authentication Filter here later.
        // It should run before Spring Security's default UsernamePasswordAuthenticationFilter
        // to process the JWT from the request header.
        // http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Keep commented for now

        return http.build();
    }
}
