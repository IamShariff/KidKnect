package com.kidknect.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidknect.enums.UserRole;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Teacher implements User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String name;
    private String password;
    private boolean isClassTeacher;
    private int className;
    
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
	private Map<String, List<String>> subjectTeaches;

    private String address;
    private String email;
    private String phoneNumber;

    @ElementCollection
    @CollectionTable(name = "teacher_qualifications")
    @Column(name = "qualification")
    private List<String> qualifications;

    @ElementCollection
    @CollectionTable(name = "teacher_certifications")
    @Column(name = "certification")
    private List<String> certifications;

    private int teachingExperience;

    @ElementCollection
    @CollectionTable(name = "teacher_responsibilities")
    @Column(name = "responsibility")
    private List<String> responsibilities;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Override
    public Integer userId() {
        return userId;
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
    
    public String getSubjectTeachesJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(subjectTeaches);
		} catch (JsonProcessingException e) {
			e.printStackTrace(); // Handle or log the exception as needed
			return null;
		}
	}

	public void setSubjectTeachesJson(String subjectTeachesJson) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			this.subjectTeaches = mapper.readValue(subjectTeachesJson, new TypeReference<Map<String, List<String>>>() {
			});
		} catch (IOException e) {
			e.printStackTrace(); // Handle or log the exception as needed
		}
	}
}
