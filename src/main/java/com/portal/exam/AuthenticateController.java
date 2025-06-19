
package com.portal.exam;

import java.security.Principal;

import com.portal.exam.User; 
import com.portal.exam.UserService; 
import com.portal.exam.UserServiceImpl; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.web.bind.annotation.CrossOrigin; // REMOVE THIS IMPORT if it was present
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
// REMOVE THIS ANNOTATION: @CrossOrigin("*") - Let WebConfig handle CORS globally
public class AuthenticateController {

    @Autowired
    private AuthenticationManager authenticationManager;

    // **CRITICAL FIX: Inject your UserDetailsService implementation directly by its class name**
    // Based on your file structure, this is likely UserServiceImpl
    @Autowired
    private UserServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    
    public static class JwtResponse {
        private String jwtToken;
        private User user; 

        public JwtResponse(String jwtToken, User user) {
            this.jwtToken = jwtToken;
            this.user = user;
        }

        public String getJwtToken() {
            return jwtToken;
        }

        public void setJwtToken(String jwtToken) {
            this.jwtToken = jwtToken;
        }

        public User getUser() { // Getter for the User object
            return user;
        }

        public void setUser(User user) { // Setter for the User object
            this.user = user;
        }
    }

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

        // Use the injected userDetailsService
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
        
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(user);
    }

    // This is the private authenticate method. 
    private void authenticate(String username, String password) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
