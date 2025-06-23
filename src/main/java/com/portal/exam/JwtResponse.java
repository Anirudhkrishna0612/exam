package com.portal.exam;

import com.portal.exam.User; 

public class JwtResponse {

	String token;
	User user; // CRITICAL: Add the User object field

	// Constructor: Takes both token and User object
	public JwtResponse(String token, User user) { // CRITICAL: Add this constructor
		super();
		this.token = token;
		this.user = user;
	}

	// Existing constructor: Takes only token
	public JwtResponse(String token) {
		super();
		this.token = token;
	}

	// Existing no-arg constructor
	public JwtResponse() {
		super();
		// TODO Auto-generated constructor stub (you can remove this comment)
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	// CRITICAL: Add Getter and Setter for User object
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
