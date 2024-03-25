package com.kidknect.responsedto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfileResponseDto {
	
	private Integer studentId;
	private int admissionId;
	private String name;
	private int className;
	private int rollNumber;
	private String address;
	private String dateOfBirth;
	private String classTeacher;
	private String email;
	private String phoneNumber;
	private String emergencyContact;
	private String parentGuardianName;
	private String parentGuardianEmail;
	private List<String> medicalConditions;

}
