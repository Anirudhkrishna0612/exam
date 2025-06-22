package com.portal.exam;

public class UserFoundException extends Exception {
	
	public UserFoundException() {
        super("User with this Username is already in the database!, try new one ");
    }

    
    public UserFoundException(String message) {
        super(message);
    }

}
