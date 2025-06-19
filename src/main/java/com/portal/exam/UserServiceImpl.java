
package com.portal.exam;

import com.portal.exam.User; // Your custom User entity
import com.portal.exam.Role; // Assuming you have a Role model
import com.portal.exam.UserRole; // Assuming you have a UserRole entity
import com.portal.exam.RoleRepository;
import com.portal.exam.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {
        User localUser = this.userRepository.findByUsername(user.getUsername());
        if (localUser != null) {
            System.out.println("User is already there !!");
            throw new Exception("User already present !!");
        } else {
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }
            user.getUserRoles().addAll(userRoles);
            localUser = this.userRepository.save(user);
        }
        return localUser;
    }

    // **CRITICAL FIX: ADD THE IMPLEMENTATION FOR getUserByUsername**
    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username); // This calls the method from your UserRepository
    }

    // --- Implementation of Spring Security's UserDetailsService method ---
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (user.getUserRoles() != null) {
            for (com.portal.exam.UserRole userRole : user.getUserRoles()) {
                authorities.add(new SimpleGrantedAuthority(userRole.getRole().getRoleName()));
            }
        }
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    // **CRITICAL: Add the deleteUser method from your UserService interface**
    @Override
    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }
}
