// src/main/java/com/portal/exam/service/UserService.java (Adjust package)
package com.portal.exam;

import com.portal.exam.User; // Ensure this imports your custom User entity
import com.portal.exam.UserRole; // Assuming you have a UserRole entity
import java.util.Set;

public interface UserService {
    
    // Creating user
    public User createUser(User user, Set<UserRole> userRoles) throws Exception;

    // **CRITICAL FIX: Change method name from getUser to getUserByUsername**
    public User getUserByUsername(String username);

    // delete user by id
    public void deleteUser(Long userId);

    // Add any other methods you expect your UserService to have
}
