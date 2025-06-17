package com.portal.exam;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Creating user
    @Override
    public User createUser(User user, Set<UserRole> userRoles) throws Exception {

        User local = this.userRepository.findByUsername(user.getUsername());
        if (local != null) {
            System.out.println("User is already there !!");
            throw new Exception("User already present !!");
        } else {
            // Create user
            for (UserRole ur : userRoles) {
                // Check if the role already exists by name
                Role existingRole = roleRepository.findByRoleName(ur.getRole().getRoleName());
                if (existingRole == null) {
                    // If role doesn't exist, save the new role
                    roleRepository.save(ur.getRole());
                } else {
                    // If role exists, use the existing role (important for foreign key)
                    ur.setRole(existingRole);
                }
            }

            // Ensure the user's userRoles collection is populated with managed entities
            user.getUserRoles().addAll(userRoles);
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

}