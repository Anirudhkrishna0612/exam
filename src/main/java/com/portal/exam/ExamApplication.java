// com.portal.exam.ExamApplication.java

package com.portal.exam;

import com.portal.exam.Role;
import com.portal.exam.User;
import com.portal.exam.UserRole;
import com.portal.exam.RoleRepository;
import com.portal.exam.UserRepository;
import com.portal.exam.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Import BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder; // Import PasswordEncoder interface

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ExamApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamApplication.class, args);
	}

    // **CRITICAL: Re-adding the PasswordEncoder bean here.**
    // You MUST delete PasswordEncoderConfig.java if you use this.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CommandLineRunner to initialize roles and an optional admin user
    @Bean
    public CommandLineRunner initDatabase(RoleRepository roleRepository,
                                          UserRepository userRepository,
                                          UserService userService,
                                          BCryptPasswordEncoder passwordEncoder) { // Using the bean defined above
        return args -> {
            System.out.println("Starting Database Initialization...");

            // --- 1. Ensure Roles Exist ---
            Role normalRole = roleRepository.findByRoleName("NORMAL");
            if (normalRole == null) {
                normalRole = new Role();
                normalRole.setRoleName("NORMAL");
                normalRole = roleRepository.save(normalRole); // Save and get the managed entity
                System.out.println("Created 'NORMAL' role.");
            }

            Role adminRole = roleRepository.findByRoleName("ADMIN");
            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setRoleName("ADMIN");
                adminRole = roleRepository.save(adminRole); // Save and get the managed entity
                System.out.println("Created 'ADMIN' role.");
            }

            // --- 2. Create an initial ADMIN user if not exists ---
            if (userRepository.findByUsername("ani0612") == null) {
                User adminUser = new User();
                adminUser.setFirstName("Anirudh");
                adminUser.setLastName("Krishna");
                adminUser.setUsername("ani0612");
                adminUser.setPassword(passwordEncoder.encode("ani")); // Encrypt the password
                adminUser.setPhone("1234567890");
                adminUser.setEmail("ani@gmail.com");
                adminUser.setProfile("default.png");
                adminUser.setEnabled(true);

                Set<UserRole> userRoleSet = new HashSet<>();
                UserRole userRole = new UserRole();
                userRole.setRole(adminRole);
                userRole.setUser(adminUser);
                userRoleSet.add(userRole);
                adminUser.setUserRoles(userRoleSet);

                try {
                    User createdUser = userService.createUser(adminUser, userRoleSet);
                    System.out.println("Initial ADMIN user 'ani0612' created successfully!");
                } catch (Exception e) {
                    System.err.println("Failed to create initial ADMIN user 'ani0612': " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Initial ADMIN user 'ani0612' already exists.");
            }
            System.out.println("Database Initialization Complete.");
        };
    }
}
