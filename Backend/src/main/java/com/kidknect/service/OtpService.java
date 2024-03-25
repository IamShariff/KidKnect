package com.kidknect.service;

import java.time.Instant;

import com.kidknect.requestdto.EmailOtpRequestDto;
import com.kidknect.responsedto.EmailOtpResponseDto;

/**
 * Service interface for handling OTP (One-Time Password) related operations.
 */
public interface OtpService {

	/**
	 * Check if the OTP is expired based on its creation time.
	 *
	 * @param createdTime The time when the OTP was created.
	 * @return {@code true} if the OTP is expired, {@code false} otherwise.
	 */
	boolean isOtpExpired(Instant createdTime);

	/**
	 * Check if the user has already generated an OTP.
	 *
	 * @param email The email address of the user.
	 * @return {@code true} if the user has already generated an OTP, {@code false}
	 *         otherwise.
	 */
	boolean userAlreadyGeneratedOtp(String email);

	/**
	 * Check if the user is blocked.
	 *
	 * @param email The email address of the user.
	 * @return {@code true} if the user is blocked, {@code false} otherwise.
	 */
	boolean isUserBlocked(String email);

	/**
	 * Encrypt the generated OTP for security.
	 *
	 * @param generatedOTP The generated OTP to encrypt.
	 * @return The encrypted OTP.
	 */
	String encryptOtp(Integer generatedOTP);

	/**
	 * Generate a new OTP for the specified email address.
	 *
	 * @param email The email address of the user.
	 * @return The generated OTP.
	 */
	Integer generateOTP(String email);

	/**
	 * Save the generated OTP for the specified email address.
	 *
	 * @param email             The email address of the user.
	 * @param generatedEmailOtp The generated OTP to save.
	 */
	void saveOtp(String email, Integer generatedEmailOtp);

	/**
	 * Check if the provided OTP is valid for the specified email address.
	 *
	 * @param email    The email address of the user.
	 * @param emailOtp The OTP to validate.
	 * @return {@code true} if the OTP is valid, {@code false} otherwise.
	 */
	boolean isOtpValid(String email, Integer emailOtp);

	/**
	 * Send an OTP to the user's email address based on the request information.
	 *
	 * @param emailOtpRequestDto The request object containing email and other
	 *                           details.
	 * @return The response containing the status of the OTP sending operation.
	 */
	EmailOtpResponseDto sendOtpToEmail(EmailOtpRequestDto emailOtpRequestDto);

}
