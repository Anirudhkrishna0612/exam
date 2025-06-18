package com.portal.exam; 

/**
 * Custom exception to be thrown when a user is not found during authentication.
 * This class extends Exception, making it a checked exception, which means
 * it must be either caught or declared in the method signature.
 */
public class UserNotFoundException extends Exception {

    /**
     * Default constructor with a predefined message.
     */
    public UserNotFoundException() {
        super("User with this Username not found in the database!");
    }

    /**
     * Constructor allowing a custom message to be passed.
     * @param message The detailed message for the exception.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
