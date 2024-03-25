package com.kidknect.service;

import com.kidknect.responsedto.EmailOtpResponseDto;

/**
 * Service interface for authentication-related operations.
 */
public interface AuthService {

    /**
     * Get the email of the currently logged-in user.
     *
     * @return The email of the logged-in user.
     */
    String getLoginedUserEmail();

    /**
     * Initiate the process of forgetting a user's password.
     *
     * @param email The email of the user who forgot the password.
     * @return An EmailOtpResponseDto containing the result of the operation.
     */
    EmailOtpResponseDto forgetPassword(String email);

    /**
     * Verify the OTP (One-Time Password) for a user's email.
     *
     * @param email The email of the user.
     * @param otp   The One-Time Password to verify.
     * @return {@code true} if the OTP is valid, {@code false} otherwise.
     */
    Boolean verifyOtp(String email, Integer otp);

    /**
     * Reset the password for a user's email.
     *
     * @param email    The email of the user.
     * @param password The new password to set.
     * @return {@code true} if the password is successfully reset, {@code false} otherwise.
     */
    Boolean resetPassword(String email, String password);

}
