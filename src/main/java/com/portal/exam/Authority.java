// com.portal.exam.Authority.java

package com.portal.exam;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority{

	private String authority;
	
	public Authority(String authority) {
		// Ensure the constructor correctly sets the 'authority' field
		this.authority = authority;
	}
	
	@Override
	public String getAuthority() {
		// **CRITICAL FIX: Return the stored authority string, not null!**
		// This will return "ROLE_ADMIN", "ROLE_NORMAL", or whatever was passed to the constructor.
		return this.authority;
	}
}
