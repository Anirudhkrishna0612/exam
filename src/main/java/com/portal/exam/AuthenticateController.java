package com.portal.exam; // Make sure this package name is correct

import java.security.Principal;

import com.portal.exam.User;
import com.portal.exam.UserService;
import com.portal.exam.UserServiceImpl;
import com.portal.exam.UserNotFoundException; // Assuming this is where UserNotFoundException is located
import com.portal.exam.JwtRequest; // Assuming this path for JwtRequest
import com.portal.exam.JwtResponse; // Assuming this path for JwtResponse
import com.portal.exam.JwtUtil; // Assuming this path for JwtUtil

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // Or your specific UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Explicitly import if needed
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthenticateController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // generate token endpoint
    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {

        try {
            authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            throw new UserNotFoundException("User not found with username: " + jwtRequest.getUsername());
        } catch (DisabledException e) {
            e.printStackTrace();
            throw new Exception("USER DISABLED: " + e.getMessage());
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            throw new Exception("INVALID CREDENTIALS: " + e.getMessage());
        }

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = this.jwtUtil.generateToken(userDetails);
        
        // Fetch your actual domain User object
        User currentUser = this.userService.getUserByUsername(jwtRequest.getUsername());

        // Return the token AND your custom User object in the response
        return ResponseEntity.ok(new JwtResponse(token, currentUser));
    }
    
    //Returns the detail of current user
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        String username = principal.getName();
        
        // Fetch your actual custom User from the database using the domain UserService
        User user = this.userService.getUserByUsername(username);
        
        // --- DEBUG STATEMENTS ADDED ---
        System.out.println("DEBUG: In /current-user for username: " + username);
        System.out.println("DEBUG: User object fetched: " + user); // This will show if user is null or a User object
        // --- END DEBUG STATEMENTS ---

        if (user == null) {
            System.out.println("DEBUG: User not found in DB for /current-user, returning 404.");
            return ResponseEntity.notFound().build();
        }
        
        System.out.println("DEBUG: User found for /current-user, returning OK.");
        return ResponseEntity.ok(user);
    }

    // This is the private authenticate method.
    private void authenticate(String username, String password) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
