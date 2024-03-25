package com.kidknect.responsedto;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherUpdateResponseDto {
	
	private Integer userId;
	private String name;
	private boolean isClassTeacher;
	private String className;
	private Map<String, List<String>> subjectTeaches;
	private String address;
	private String email;
	private String phoneNumber;
	private List<String> qualifications;
	private List<String> certifications;
	private int teachingExperience;
	private List<String> responsibilities;
	
	public String getSubjectTeachesJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(subjectTeaches);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setSubjectTeachesJson(String subjectTeachesJson) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			this.subjectTeaches = mapper.readValue(subjectTeachesJson, new TypeReference<Map<String, List<String>>>() {
			});
		} catch (IOException e) {
			e.printStackTrace(); 
		}
	}

}
