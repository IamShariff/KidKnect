package com.kidknect.responsedto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentUpdateResponseDto {
	
	private Integer studentId;
	private String address;
	private String dateOfBirth;
	private String emergencyContact;
	private String parentGuardianName;
	private String parentGuardianEmail;
	private List<String> medicalConditions;

}
