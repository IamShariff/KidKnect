package com.kidknect.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {

	public static final String FIELD_EMAIL = "email";
	public static final String FIELD_OTP = "otp";
	public static final String MESSAGE_NOT_FOUND = "Not found";
	public static final String MESSAGE_ACCOUNT_BLOCKED = "Account Blocked";
	public static final String MESSAGE_EXPIRED_OTP = "Expired Otp";
	public static final String MESSAGE_INCORRECT_OTP = "Incorrect Otp !!";
	public static final String MESSAGE_ATTEMPTS_REMAINING = " attempts remaining";
	public static final String MESSAGE_ACCOUNT_DEACTIVATED = "Account Deactivated";
	public static final String MESSAGE_SENDING_OTP_ERROR = "Exception occurred during sending OTP";
	public static final String MESSAGE_WAIT_FOR_OTP = "Otp already generated wait for 30 sec";

	public static final Map<String, String> EMAIL_TYPE;

	static {
		Map<String, String> map = new HashMap<>();
		map.put("Register", "Registration to KidKnect");
		map.put("Login", "Login to KidKnect");
		map.put("Kyc", "For Kyc");
		EMAIL_TYPE = Collections.unmodifiableMap(map);
	}

	// Add new constants for TestServiceImpl
	public static final String MESSAGE_TEST_RESULT_EMPTY = "Test result is empty";
	public static final String MESSAGE_TEST_CSV_ERROR = "Error processing CSV file.";
	public static final String MESSAGE_TEST_DETAILS_ADDED_SUCCESS = "Test details from CSV added successfully";
	public static final String MESSAGE_TEST_DETAILS_FROM_CSV = "Test details added successfully";
	public static final String MESSAGE_TEACHER_NOT_FOUND = "Teacher not found";
	public static final String MESSAGE_TEACHER_NOT_TEACH_CLASS = "Teacher does not teach the subject to this class";
	public static final String FIELD_TEACHER = "Teacher";
	public static final String PRINCIPAL_ALREADY_EXIST = "Principal already exist";
	public static final String FIELD_PRINCIPAL = "Principal";

	public static final String TEACHERS_ONBOARDED_SUCCESSFULLY = "New teachers onboarded successfully: ";
	public static final String TEACHERS_ONBOARDED = "New teachers onboarded: ";
	public static final String TEACHERS_SKIPPED = ". Teachers skipped due to existing email or phone: ";

}
