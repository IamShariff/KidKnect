package com.kidknect.service;

/**
 * Service interface for sending emails and notifications using JavaMail.
 */
public interface JavamailService {

	/**
	 * Send an email to the specified recipient with the given subject and OTP
	 * (One-Time Password).
	 *
	 * @param recipientEmail    The email address of the recipient.
	 * @param subject           The subject of the email.
	 * @param generatedEmailOtp The OTP (One-Time Password) to include in the email.
	 */
	void sendEmail(String recipientEmail, String subject, Integer generatedEmailOtp);

	/**
	 * Send a notification email to the specified recipient with the given subject
	 * and message.
	 *
	 * @param recipientEmail The email address of the recipient.
	 * @param subject        The subject of the notification.
	 * @param message        The message content of the notification.
	 */
	void sendNotification(String recipientEmail, String subject, String message);
}
