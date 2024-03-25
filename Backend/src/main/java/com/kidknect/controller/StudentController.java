package com.kidknect.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.kidknect.requestdto.AddStudentsRequestDto;
import com.kidknect.requestdto.UpdateStudentProfileRequestDto;
import com.kidknect.responsedto.AttendenceListResponseDto;
import com.kidknect.responsedto.ResponseHandler;
import com.kidknect.responsedto.StudentProfileResponseDto;
import com.kidknect.responsedto.TodayAttendanceResponseDto;
import com.kidknect.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for handling student-related operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

	private final StudentService studentService;

	/**
	 * Endpoint to onboard students for a given class.
	 *
	 * @param className            The class name or identifier
	 * @param addStudentRequestDto List of students to onboard
	 * @return ResponseEntity indicating the status of the operation
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/{className}/add-students")
	@Operation(description = "To onboard students")
	ResponseEntity<Object> onboardStudents(@PathVariable int className,
			@RequestBody List<AddStudentsRequestDto> addStudentRequestDto) {
		return ResponseHandler.generateResponse(studentService.addStudents(className, addStudentRequestDto),
				HttpStatus.CREATED);
	}

	/**
	 * Endpoint to onboard students by uploading a CSV file.
	 *
	 * @param className The class name or identifier
	 * @param file      The CSV file containing student data
	 * @return ResponseEntity indicating the status of the operation
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/{className}/upload-csv")
	@Operation(description = "To onboard students by CSV file")
	ResponseEntity<String> onboardStudentsFromCSV(@PathVariable("className") int className,
			@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("Please upload a CSV file.");
		}
		try {
			String result = studentService.addStudentsFromCSV(className, file);
			return ResponseEntity.ok(result);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process CSV file.");
		}
	}

	/**
	 * Endpoint to update a student's profile.
	 *
	 * @param updateProfileRequestDto The updated profile information
	 * @return ResponseEntity indicating the status of the operation
	 */
	@PreAuthorize("hasAuthority('STUDENT')")
	@PutMapping("/update-student")
	@Operation(description = "To update profile")
	ResponseEntity<Object> updateProfile(@RequestBody UpdateStudentProfileRequestDto updateProfileRequestDto) {
		return ResponseHandler.generateResponse("Profile Updated Successfully", HttpStatus.CREATED,
				studentService.updateStudentProfile(updateProfileRequestDto));
	}

	/**
	 * Endpoint to get a student's profile.
	 *
	 * @return ResponseEntity containing the student's profile
	 */
	@PreAuthorize("hasAuthority('STUDENT')")
	@GetMapping("/profile")
	@Operation(description = "To get profile")
	ResponseEntity<Object> getProfile() {
		return ResponseHandler.generateResponse("Profile fetched Successfully", HttpStatus.OK,
				studentService.getProfile());
	}

	/**
	 * Endpoint to get attendance statistics for a student within a range of months.
	 *
	 * @param startDate The start date of the range (format: yyyy-MM-dd)
	 * @param endDate   The end date of the range (format: yyyy-MM-dd)
	 * @return ResponseEntity containing the attendance statistics
	 */
	@PreAuthorize("hasAuthority('STUDENT')")
	@GetMapping("/attendence-months/{startDate}/{endDate}")
	@Operation(description = "To get attendance for months")
	ResponseEntity<Object> getAttendanceStatisticsForMonths(@PathVariable String startDate,
			@PathVariable String endDate) {
		List<AttendenceListResponseDto> attendanceForMonths = studentService.getAttendenceForMonths(startDate, endDate);
		return ResponseEntity.ok(attendanceForMonths);
	}

	/**
	 * Endpoint to get student details by roll number.
	 *
	 * @param rollNo The roll number of the student
	 * @return ResponseEntity containing the student details
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@GetMapping("/{rollNo}")
	@Operation(description = "Get student details by roll no")
	ResponseEntity<Object> getStudentByRollNo(@PathVariable int rollNo) {
		StudentProfileResponseDto studentDto = studentService.getStudentByRollNo(rollNo);
		return ResponseHandler.generateResponse("Student details fetched Successfully", HttpStatus.OK, studentDto);
	}

	/**
	 * Endpoint to get a student's attendance for today.
	 *
	 * @param rollNo The roll number of the student
	 * @return ResponseEntity containing the attendance for today
	 */
	@PreAuthorize("hasAuthority('STUDENT')")
	@GetMapping("/attendance/today/{rollNo}")
	@Operation(description = "Get student's attendance for today")
	ResponseEntity<Object> getAttendanceForToday(@PathVariable int rollNo) {
		TodayAttendanceResponseDto attendance = studentService.getAttendanceForToday(rollNo);
		return ResponseHandler.generateResponse("Attendance for today fetched Successfully", HttpStatus.OK, attendance);
	}
}
