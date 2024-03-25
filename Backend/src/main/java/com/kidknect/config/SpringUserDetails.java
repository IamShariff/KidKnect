package com.kidknect.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kidknect.model.User;

/**
 * Custom UserDetails implementation for Spring Security.
 */
public class SpringUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	private String email;
	private String password;
	private List<GrantedAuthority> authorities;

	/**
	 * Constructor to create SpringUserDetails from a User entity.
	 *
	 * @param userInfo The User entity from which to create SpringUserDetails
	 */
	public SpringUserDetails(User userInfo) {
		email = userInfo.email();
		password = userInfo.password();
		authorities = new ArrayList<>();
		// Add the user's role as a GrantedAuthority
		authorities.add(new SimpleGrantedAuthority(userInfo.role().getUserType().toUpperCase()));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Return the list of GrantedAuthorities for the user
		return authorities;
	}

	@Override
	public String getPassword() {
		// Return the user's password
		return password;
	}

	@Override
	public String getUsername() {
		// Return the user's email as the username
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// Return true if the user's account is not expired
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// Return true if the user's account is not locked
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// Return true if the user's credentials are not expired
		return true;
	}

	@Override
	public boolean isEnabled() {
		// Return true if the user's account is enabled
		return true;
	}
}
