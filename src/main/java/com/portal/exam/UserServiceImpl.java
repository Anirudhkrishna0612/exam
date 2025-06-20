// com.portal.exam.service.impl.UserServiceImpl.java

package com.portal.exam;

import com.portal.exam.User;
import com.portal.exam.UserRole;
import com.portal.exam.Role;
import com.portal.exam.RoleRepository;
import com.portal.exam.UserRepository;
import com.portal.exam.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Keep this import
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // **CRITICAL (Minor change): Autowire directly as BCryptPasswordEncoder**
    // This is less flexible for swapping encoders, but eliminates any doubt about matching types.
    @Autowired
    private BCryptPasswordEncoder passwordEncoder; 

    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {
        User localUser = this.userRepository.findByUsername(user.getUsername());
        if (localUser != null) {
            System.out.println("User is already there !!");
            throw new Exception("User already present !!");
        } else {
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));

            for (UserRole ur : userRoles) {
                ur.setUser(user);
            }
            
            user.getUserRoles().addAll(userRoles); 
            localUser = this.userRepository.save(user);
        }
        return localUser;
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (user.getUserRoles() != null) {
            for (com.portal.exam.UserRole userRole : user.getUserRoles()) {
                if (userRole.getRole() != null && userRole.getRole().getRoleName() != null) {
                    authorities.add(new SimpleGrantedAuthority(userRole.getRole().getRoleName()));
                }
            }
        }
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }
}
