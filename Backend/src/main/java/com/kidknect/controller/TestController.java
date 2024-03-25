package com.kidknect.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kidknect.requestdto.CsvTestDetailsRequestDto;
import com.kidknect.requestdto.TestDetailsRequestDto;
import com.kidknect.responsedto.ResponseHandler;
import com.kidknect.service.TestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for handling test-related operations.
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Test Controller")
@RequestMapping("/test")
public class TestController {

	private final TestService testService;

	/**
	 * Endpoint to add test details.
	 *
	 * @param requestDto The test details request object
	 * @return ResponseEntity indicating the status of the operation
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/add-testdetails")
	@Operation(summary = "Add Test Details", description = "Endpoint to add test details")
	@ApiResponse(responseCode = "201", description = "Test details added successfully")
	public ResponseEntity<String> addTestDetails(
			@RequestBody @Parameter(description = "Test details request object") TestDetailsRequestDto requestDto) {
		String response = testService.addTestDetails(requestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * Endpoint to add test details from a CSV file.
	 *
	 * @param className  The class name or identifier
	 * @param subject    The subject of the test
	 * @param testName   The name of the test
	 * @param totalMarks The total marks for the test
	 * @param testDate   The date of the test in the format dd/MM/yyyy
	 * @param file       The CSV file containing test details
	 * @return ResponseEntity indicating the status of the operation
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/test-details/upload-csv")
	@Operation(summary = "Add Test Details from CSV", description = "Endpoint to add test details from a CSV file")
	@ApiResponse(responseCode = "200", description = "Test details added successfully")
	@ApiResponse(responseCode = "500", description = "Error processing CSV file", content = @Content)
	public ResponseEntity<String> addTestDetailsFromCSVFile(@RequestParam("className") int className,
			@RequestParam("subject") String subject, @RequestParam("testName") String testName,
			@RequestParam("totalMarks") int totalMarks,
			@RequestParam("testDate") @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/2024$", message = "Invalid date format. Use dd/MM/yyyy") String testDate,
			@RequestParam("file") MultipartFile file) {

		CsvTestDetailsRequestDto csvTestDetailsRequestDto = CsvTestDetailsRequestDto.builder().className(className)
				.subject(subject).testName(testName).totalMarks(totalMarks).testDate(testDate).build();

		String result = testService.addTestDetailsFromCSV(csvTestDetailsRequestDto, file);
		return ResponseEntity.ok(result);

	}

	/**
	 * Endpoint to get test results.
	 *
	 * @param testName The name of the test
	 * @param subject  The subject of the test (optional)
	 * @return ResponseEntity containing the test results
	 */
	@PreAuthorize("hasAuthority('STUDENT')")
	@GetMapping("/test-results")
	@Operation(summary = "Get Test Results", description = "Endpoint to get test results")
	@ApiResponse(responseCode = "200", description = "Result fetched successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Object.class)))
	public ResponseEntity<Object> getTestResults(
			@RequestParam @Parameter(description = "Name of the test") String testName,
			@RequestParam(required = false) @Parameter(description = "Subject of the test") String subject) {
		return ResponseHandler.generateResponse("Result fetched successfully", HttpStatus.OK,
				testService.getResultForTestName(testName, subject));
	}

}
