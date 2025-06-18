package com.portal.exam;

import java.util.HashSet;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext; // NEW IMPORT: Import ApplicationContext


@SpringBootApplication
public class ExamApplication implements CommandLineRunner {
    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(ExamApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting code");

       
        UserService userService = context.getBean(UserService.class);

        User user = new User();

        user.setFirstName("Anirudh");
        user.setLastName("Krishna");
        user.setUsername("ani0612");
        user.setPassword("ani"); 
        user.setPhone("1234567890");
        user.setEmail("ani@gmail.com");
        user.setProfile("default.png");

        Role role1 = new Role();
        role1.setRoleName("ADMIN");  

        Set<UserRole> userRoleSet = new HashSet<>();
        UserRole userRole = new UserRole();
        userRole.setRole(role1); 
        userRole.setUser(user);  

        userRoleSet.add(userRole);

        try {
            User user1 = userService.createUser(user, userRoleSet); 
            System.out.println("User created successfully: " + user1.getUsername());
        } catch (Exception e) {
             
            System.err.println("User setup failed or user already exists: " + e.getMessage());
            
        }

    }
}