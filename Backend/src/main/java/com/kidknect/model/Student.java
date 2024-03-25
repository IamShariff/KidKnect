package com.kidknect.model;

import java.util.ArrayList;
import java.util.List;

import com.kidknect.enums.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer studentId;
	private String password;
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
	@Enumerated(EnumType.STRING)
	private UserRole userRole;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<Attendence> attendances = new ArrayList<>();

	@Override
	public Integer userId() {

		return studentId;
	}

	@Override
	public String userName() {

		return name;
	}

	@Override
	public String password() {

		return password;
	}

	@Override
	public String mobile() {

		return phoneNumber;
	}

	@Override
	public String email() {

		return email;
	}

	@Override
	public UserRole role() {

		return userRole;
	}
	
	public void addAttendance(Attendence attendance) {
	    List<Attendence> newAttendanceList = new ArrayList<>(attendances);
	    newAttendanceList.add(attendance);
	    this.attendances = newAttendanceList;
	}

}
