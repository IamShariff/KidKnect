package com.kidknect.enums;

import java.util.Arrays;
import java.util.Optional;

public enum VerificationStatus {

	/**
	 * Represents the "verified" verification status.
	 */
	VERIFIED("verified"),

	/**
	 * Represents the "pending" verification status.
	 */
	PENDING("pending");

	private String verificationStatus;

	private VerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	/**
	 * Get the verification status.
	 *
	 * @return The verification status.
	 */
	public String getVerificationStatus() {
		return this.verificationStatus;
	}

	/**
	 * Check if the given verification status exists as a valid VerificationStatus.
	 *
	 * @param verificationStatus The verification status to check.
	 * @return {@code true} if the verification status exists, {@code false}
	 *         otherwise.
	 */
	public static boolean hasVerificationStatus(String verificationStatus) {
		for (VerificationStatus status : VerificationStatus.values()) {
			if (status.getVerificationStatus().equalsIgnoreCase(verificationStatus)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieve the VerificationStatus based on the given verification status
	 * string.
	 *
	 * @param verificationStatus The verification status to search for.
	 * @return An Optional containing the matched VerificationStatus, or an empty
	 *         Optional if not found.
	 */
	public static Optional<VerificationStatus> fromString(String verificationStatus) {
		return Arrays.stream(VerificationStatus.values())
				.filter(status -> status.getVerificationStatus().equalsIgnoreCase(verificationStatus)).findFirst();
	}

}
