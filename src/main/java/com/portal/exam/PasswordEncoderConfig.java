package com.portal.exam;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Dedicated configuration class for providing the PasswordEncoder bean.
 * This helps to break circular dependencies when other security-related
 * components (like UserDetailsService) depend on PasswordEncoder,
 * and the main SecurityConfig might depend on those components.
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * Defines the password encoder bean.
     * BCryptPasswordEncoder is highly recommended for securely hashing passwords.
     *
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
