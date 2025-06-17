package com.portal.exam;

import java.util.Set;

public interface UserService {
	
	//Creating user
	public User createUser(User user,Set<UserRole> userRoles) throws Exception;
	//Get user by username 
	public User getUser(String username);
	//delete user by id
	public void deleteUser(Long userId);

}
