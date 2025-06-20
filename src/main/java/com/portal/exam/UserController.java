// com.portal.exam.controller.UserController.java

package com.portal.exam;

import com.portal.exam.Role; // Use the Role model
import com.portal.exam.User; // Use the User model
import com.portal.exam.UserRole; // Use the UserRole model
import com.portal.exam.RoleRepository; // Import RoleRepository
import com.portal.exam.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleRepository roleRepository; // Inject RoleRepository to fetch roles
	
	// Creating User for signup (defaulting to NORMAL role)
	@PostMapping("/")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		try {
			user.setProfile("default.png"); // Set default profile picture

			Set<UserRole> roles = new HashSet<>();
			
			// **CRITICAL FIX: Fetch the 'NORMAL' role from the database.**
			// Do NOT hardcode roleId or create new Role() objects for existing roles.
			Role normalRole = roleRepository.findByRoleName("NORMAL"); 
			
			if (normalRole == null) {
				System.err.println("Error: 'NORMAL' role not found in database. Please ensure it's initialized.");
				// Return an appropriate error response if the role isn't found
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
			}
			
			UserRole userRole = new UserRole();
			userRole.setUser(user);        // Set the user (will be saved with user)
			userRole.setRole(normalRole);  // **Set the fetched, managed 'NORMAL' role**
			
			roles.add(userRole);
			
			User createdUser = this.userService.createUser(user, roles); // Pass user and its roles to service
			return new ResponseEntity<>(createdUser, HttpStatus.CREATED);

		} catch (Exception e) {
			System.err.println("Error during user signup: " + e.getMessage());
			e.printStackTrace(); // Log full stack trace for detailed debugging
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Generic error response
		}
	}
	
	@GetMapping("/{username}")
	public User getUser(@PathVariable("username")String username) {
		return this.userService.getUserByUsername(username);
	}
	
	// Delete the user by id
	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable("userId") Long userId ) {
		this.userService.deleteUser(userId);
	}
}
