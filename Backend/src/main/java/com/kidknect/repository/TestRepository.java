package com.kidknect.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kidknect.model.TestDetails;

public interface TestRepository extends JpaRepository<TestDetails, Integer> {

	List<TestDetails> findByTestNameAndRollNumber(String testName, int rollNumber);

	List<TestDetails> findByTestNameAndSubjectAndRollNumber(String testName, String subject, int rollNumber);

}
