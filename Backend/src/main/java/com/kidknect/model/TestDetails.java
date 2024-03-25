package com.kidknect.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer testId;
	private int rollNumber;
	private int className;
	private String subject;
	private String testName;
	private boolean isGiven;
	private int marksObtained;
	private int totalMarks;
	private LocalDate testDate;
}
