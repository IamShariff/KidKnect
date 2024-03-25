package com.kidknect.requestdto;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;

public record UpdateTeacherProfileRequestDto(boolean isClassTeacher,
		@Null(message = "Class name must be between 1 and 12 or null") @Min(value = 1, message = "Class name must be between 1 and 12") @Max(value = 12, message = "Class name must be between 1 and 12") int className,
		Map<String, List<String>> subjectTeaches, String address, List<String> qualifications,
		List<String> certifications, int teachingExperience, List<String> responsibilities) {

}
