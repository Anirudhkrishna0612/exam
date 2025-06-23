// src/main/java/com/portal/exam/ExamApplication.java
package com.portal.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ExamApplication implements CommandLineRunner {

	@Autowired
	private UserService userService; // Inject UserService

	@Autowired
	private PasswordEncoder passwordEncoder; // Inject PasswordEncoder (from BCryptConfig)

	@Autowired
	private RoleRepository roleRepository; // Inject RoleRepository

	public static void main(String[] args) {
		SpringApplication.run(ExamApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Starting code...");

		// **CRITICAL FIX: Ensure Roles (ADMIN, NORMAL) and an ADMIN user exist on startup**
		try {
			// Check if ADMIN role exists, create if not
			Role adminRole = roleRepository.findByRoleName("ADMIN");
			if (adminRole == null) {
				adminRole = new Role();
				adminRole.setRoleName("ADMIN");
				adminRole = roleRepository.save(adminRole);
				System.out.println("ADMIN role created.");
			}

			// Check if NORMAL role exists, create if not
			Role normalRole = roleRepository.findByRoleName("NORMAL");
			if (normalRole == null) {
				normalRole = new Role();
				normalRole.setRoleName("NORMAL");
				normalRole = roleRepository.save(normalRole);
				System.out.println("NORMAL role created.");
			}

			// Create initial admin user if not exists
			User adminUser = userService.getUserByUsername("admin");
			if (adminUser == null) {
				adminUser = new User();
				adminUser.setUsername("admin");
				adminUser.setPassword(passwordEncoder.encode("admin")); // Hash password
				adminUser.setFirstName("Admin");
				adminUser.setLastName("User");
				adminUser.setEmail("admin@example.com");
				adminUser.setPhone("1234567890");
				adminUser.setProfile("default.png");
				adminUser.setEnabled(true);

				Set<UserRole> userRoles = new HashSet<>();
				UserRole adminUserRole = new UserRole();
				adminUserRole.setUser(adminUser);
				adminUserRole.setRole(adminRole); // Assign ADMIN role

				userRoles.add(adminUserRole);

				userService.createUser(adminUser, userRoles); // Use the service to create user
				System.out.println("Default ADMIN user created.");
			} else {
				System.out.println("ADMIN user already exists.");
			}

		} catch (UserFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error during initial data setup: " + e.getMessage());
		}
	}
}
