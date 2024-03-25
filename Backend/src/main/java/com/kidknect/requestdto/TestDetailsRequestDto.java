package com.kidknect.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

import jakarta.validation.constraints.Pattern;

@Data
public class TestDetailsRequestDto {

	private int className;
	private String subject;
	private String testName;
	private int totalMarks;
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/2024$", message = "Invalid date format. Use dd/MM/yyyy")
	private String testDate;
	private List<TestDetailEntry> testDetails;

	@Data
	@RequiredArgsConstructor
	@AllArgsConstructor
	public static class TestDetailEntry {
		private int rollNumber;
		private boolean isGiven;
		private int marksObtained;
	}
}
