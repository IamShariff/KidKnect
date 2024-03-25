package com.kidknect.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kidknect.config.JwtService;
import com.kidknect.exception.NotFoundException;
import com.kidknect.requestdto.JwtLoginRequestDto;
import com.kidknect.responsedto.ResponseHandler;
import com.kidknect.service.AuthService;

import lombok.RequiredArgsConstructor;

/**
 * Controller class for handling authentication-related endpoints.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final JwtService jwtService;
	private final AuthService authService;
	private final AuthenticationManager authenticationManager;

	/**
	 * Endpoint to authenticate a user and generate a JWT token.
	 *
	 * @param jwtRequest The JwtLoginRequestDto containing user login details
	 * @return ResponseEntity containing the generated JWT token
	 */
	@PostMapping("/login")
	public ResponseEntity<Object> authenticateAndGetToken(@RequestBody JwtLoginRequestDto jwtRequest) {
		Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.email(), jwtRequest.password()));

		if (authenticate.isAuthenticated()) {
			// User authenticated, generate JWT token
			return ResponseHandler.generateResponse("Login token generated successfully", HttpStatus.CREATED,
					jwtService.generateToken(jwtRequest));
		} else {
			// User authentication failed, throw NotFoundException
			throw new NotFoundException("ExceptionConstants.USER_DONT_EXIST", jwtRequest.email());
		}
	}

	/**
	 * Endpoint to send OTP for password reset.
	 *
	 * @param email The email of the user
	 * @return ResponseEntity indicating if OTP was sent successfully
	 */
	@PostMapping("/reset-password/send-otp/{email}")
	ResponseEntity<Object> forgetPassword(@PathVariable String email) {
		return ResponseHandler.generateResponse("Otp sent Successfully", HttpStatus.CREATED,
				authService.forgetPassword(email));
	}

	/**
	 * Endpoint to verify OTP for password reset.
	 *
	 * @param email The email of the user
	 * @param otp   The OTP entered by the user
	 * @return ResponseEntity indicating if OTP was verified successfully
	 */
	@PostMapping("/reset-password/verify-otp/{email}/{otp}")
	ResponseEntity<Object> verifyOtp(@PathVariable String email, @PathVariable Integer otp) {
		return ResponseHandler.generateResponse("Otp verified Successfully", HttpStatus.OK,
				authService.verifyOtp(email, otp));
	}

	/**
	 * Endpoint to reset password after verifying OTP.
	 *
	 * @param email    The email of the user
	 * @param password The new password
	 * @return ResponseEntity indicating if password was reset successfully
	 */
	@PostMapping("/reset-password/reset/{email}/{password}")
	ResponseEntity<Object> resetPassword(@PathVariable String email, @PathVariable String password) {
		return ResponseHandler.generateResponse("Password reset successfully", HttpStatus.CREATED,
				authService.resetPassword(email, password));
	}
}
