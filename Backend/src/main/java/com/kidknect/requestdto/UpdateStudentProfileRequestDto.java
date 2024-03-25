package com.kidknect.requestdto;

import java.util.List;

public record UpdateStudentProfileRequestDto(String address, String dateOfBirth, String emergencyContact,
		String parentGuardianName, String parentGuardianEmail, List<String> medicalConditions) {

}
