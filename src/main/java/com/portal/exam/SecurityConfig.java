package com.portal.exam; // Adjust package as per your project

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:[*]")); // Allows any port on localhost
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        config.setExposedHeaders(Arrays.asList("Authorization"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            UserDetailsService userDetailsService // Injected UserDetailsService
    ) throws Exception {

        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF
            .cors(Customizer.withDefaults()) // Use the CorsFilter bean
            .authorizeHttpRequests(authorize -> authorize
                // 1. Public Endpoints (highest precedence - accessible by anyone)
                // CRITICAL FIX: Explicitly permit full paths starting with /auth
                .antMatchers("/auth/generate-token", "/auth/current-user").permitAll() // Ensure these are correct
                .antMatchers(HttpMethod.POST, "/user/").permitAll() // User registration (signup)
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Universal CORS pre-flight for all paths

                // 2. Endpoints accessible by both ADMIN and NORMAL users (viewing content, common user actions)
                .antMatchers("/user-dashboard",
                             "/user-quizzes/**", "/quiz-attempt/**", "/quiz-result/**").hasAnyAuthority("ADMIN", "NORMAL")
                
                // Specific GET requests for viewing quizzes and categories (crucial for normal users)
                .antMatchers(HttpMethod.GET, "/quiz/active").hasAnyAuthority("ADMIN", "NORMAL")
                .antMatchers(HttpMethod.GET, "/quiz/{quizId}").hasAnyAuthority("ADMIN", "NORMAL") // View specific quiz
                .antMatchers(HttpMethod.GET, "/category/").hasAnyAuthority("ADMIN", "NORMAL") // View all categories
                .antMatchers(HttpMethod.GET, "/category/{categoryId}").hasAnyAuthority("ADMIN", "NORMAL") // View specific category
                .antMatchers(HttpMethod.GET, "/category/quiz/{categoryId}").hasAnyAuthority("ADMIN", "NORMAL") // View quizzes by category
                .antMatchers(HttpMethod.GET, "/question/quiz/active/{quizId}").hasAnyAuthority("ADMIN", "NORMAL") // For active questions in a quiz

                // 3. Endpoints accessible ONLY by ADMIN users (management, creation, deletion)
                .antMatchers("/admin/**").hasAuthority("ADMIN") // General admin paths
                .antMatchers("/user/**").hasAuthority("ADMIN") // All /user paths *except* the POST for registration, usually for user management
                
                // Restrict POST, PUT, DELETE operations on quizzes, categories, questions to ADMIN only
                .antMatchers(HttpMethod.POST, "/quiz/", "/category/", "/question/").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/quiz/", "/category/", "/question/").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/quiz/{quizId}", "/category/{categoryId}", "/question/{quesId}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/question/{quesId}").hasAuthority("ADMIN") // Admin can get a specific question

                // 4. Any other unlisted request must be authenticated (catch-all)
                .anyRequest().authenticated()
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(unauthorizedHandler) // Handle unauthorized access
            )
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless sessions for JWT
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
