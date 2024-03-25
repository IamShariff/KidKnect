package com.kidknect.requestdto;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CsvTestDetailsRequestDto {

	private int className;
	private String subject;
	private String testName;
	private int totalMarks;
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/2024$", message = "Invalid date format. Use dd/MM/yyyy")
	private String testDate;

}
