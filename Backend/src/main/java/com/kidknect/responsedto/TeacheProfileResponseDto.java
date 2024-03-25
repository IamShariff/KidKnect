package com.kidknect.responsedto;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacheProfileResponseDto {

	private Integer userId;
	private String name;
	private boolean isClassTeacher;
	private int className;
	private Map<String, List<String>> subjectTeaches;
	private String address;
	private String email;
	private String phoneNumber;
	private List<String> qualifications;
	private List<String> certifications;
	private int teachingExperience;
	private List<String> responsibilities;

}
