package com.kidknect.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.kidknect.requestdto.CsvTestDetailsRequestDto;
import com.kidknect.requestdto.TestDetailsRequestDto;
import com.kidknect.responsedto.TestNameResultResponseDto;

/**
 * Service interface for managing test-related operations.
 */
public interface TestService {

	/**
	 * Add test details from a CSV file.
	 *
	 * @param csvTestDetailsRequestDto The request object containing CSV test
	 *                                 details.
	 * @param file                     The CSV file containing test details.
	 * @return A message indicating the status of the operation.
	 */
	String addTestDetailsFromCSV(CsvTestDetailsRequestDto csvTestDetailsRequestDto, MultipartFile file);

	/**
	 * Add test details manually.
	 *
	 * @param requestDto The request object containing test details.
	 * @return A message indicating the status of the operation.
	 */
	String addTestDetails(TestDetailsRequestDto requestDto);

	/**
	 * Get test results for a specific test name and subject.
	 *
	 * @param testName The name of the test to get results for.
	 * @param subject  The subject of the test.
	 * @return A list of test name result responses.
	 */
	List<TestNameResultResponseDto> getResultForTestName(String testName, String subject);

}
