package com.kidknect.model;


import com.kidknect.enums.UserRole;


public interface User {

	Integer userId();

	String userName();

	String password();

	String mobile();

	String email();

	UserRole role();

}
