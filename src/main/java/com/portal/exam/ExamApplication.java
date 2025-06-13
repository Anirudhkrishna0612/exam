package com.portal.exam;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExamApplication implements CommandLineRunner{

	@Autowired
	private UserService userService;
	
	public static void main(String[] args) {
		
		
		SpringApplication.run(ExamApplication.class, args);
	
	
	}
	@Override
	public void run(String... args) throws Exception{
		System.out.println("Starting code");
		
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
		
		User user1 = this.userService.createUser(user, userRoleSet);
		System.out.println(user1.getUsername());
		
		
	}

}
