package com.kidknect.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestNameResultResponseDto {
	
	    private int rollNumber;
	    private String subject;
	    private String testName;
	    private boolean isGiven;
	    private int marksObtained;
	    private int totalMarks;
	    private String testDate;

}
