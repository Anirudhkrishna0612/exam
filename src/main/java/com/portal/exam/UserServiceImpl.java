package com.portal.exam;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder; // Keep this import

import java.util.ArrayList;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; 

    
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Creating user
    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {

        User local = this.userRepository.findByUsername(user.getUsername());
        if (local != null) {
            System.out.println("User is already there !!");
            throw new Exception("User already present !!");
        } else {
            // Encodes the password before saving
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));

            for (UserRole ur : userRoles) {
                Role existingRole = roleRepository.findByRoleName(ur.getRole().getRoleName());
                if (existingRole == null) {
                    roleRepository.save(ur.getRole());
                } else {
                    ur.setRole(existingRole);
                }
            }

            if (user.getUserRoles() == null) {
                user.setUserRoles(userRoles);
            } else {
                user.getUserRoles().addAll(userRoles);
            }

            local = this.userRepository.save(user);
        }

        return local;
    }

    // getting user by username
    @Override
    public User getUser(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }

    // Implementation of loadUserByUsername method from UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);

        if (user == null) {
            System.out.println("User not found: " + username);
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            new ArrayList<>() 
        );
    }
}
