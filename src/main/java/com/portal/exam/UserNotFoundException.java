package com.portal.exam; 

public class UserNotFoundException extends Exception {

    
    public UserNotFoundException() {
        super("User with this Username not found in the database!");
    }

    
    public UserNotFoundException(String message) {
        super(message);
    }
}
