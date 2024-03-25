package com.kidknect.controller;

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

import com.kidknect.requestdto.AddPrincipalRequestDto;
import com.kidknect.requestdto.AddTeachersRequestDto;
import com.kidknect.requestdto.MarkAttendanceRequestDto;
import com.kidknect.requestdto.UpdateTeacherProfileRequestDto;
import com.kidknect.responsedto.ResponseHandler;
import com.kidknect.service.StudentService;
import com.kidknect.service.TeacherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for handling teacher-related operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher")
@Tag(name = "Teacher Controller")
public class TeacherController {

	private final TeacherService teacherService;
	private final StudentService studentService;

	/**
	 * Endpoint to onboard a principal.
	 *
	 * @param addPrincipalRequestDto Details of the principal to onboard
	 * @return ResponseEntity indicating the status of the operation
	 */
	@PostMapping("/add-principal")
	@Operation(description = "To onboard a principal")
	ResponseEntity<Object> onboardPrincipal(@RequestBody AddPrincipalRequestDto addPrincipalRequestDto) {
		return ResponseHandler.generateResponse(
				"Principal onboarded successfully with id " + teacherService.addPrincipal(addPrincipalRequestDto),
				HttpStatus.CREATED);
	}

	/**
	 * Endpoint to onboard multiple teachers.
	 *
	 * @param addTeachersRequestDto List of teachers to onboard
	 * @return ResponseEntity indicating the status of the operation
	 */
	@PreAuthorize("hasAuthority('PRINCIPAL')")
	@PostMapping("/add-teachers")
	@Operation(description = "To onboard teachers")
	ResponseEntity<Object> onboardTeachers(@RequestBody List<AddTeachersRequestDto> addTeachersRequestDto) {
		return ResponseHandler.generateResponse(teacherService.addTeachers(addTeachersRequestDto), HttpStatus.CREATED);
	}

	/**
	 * Endpoint to update a teacher's profile.
	 *
	 * @param updateProfileRequestDto The updated profile information
	 * @return ResponseEntity indicating the status of the operation
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PutMapping("/update-teacher")
	@Operation(description = "To update a teacher's profile")
	ResponseEntity<Object> updateProfile(@RequestBody UpdateTeacherProfileRequestDto updateProfileRequestDto) {
		return ResponseHandler.generateResponse("Profile Updated Successfully", HttpStatus.CREATED,
				teacherService.updateTeaherProfile(updateProfileRequestDto));
	}

	/**
	 * Endpoint to get a teacher's profile.
	 *
	 * @return ResponseEntity containing the teacher's profile
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@GetMapping("/profile")
	@Operation(description = "To get a teacher's profile")
	ResponseEntity<Object> getProfile() {
		return ResponseHandler.generateResponse("Profile fetched Successfully", HttpStatus.OK,
				teacherService.getProfile());
	}

	/**
	 * Endpoint to mark attendance for students.
	 *
	 * @param requestDto Details of attendance to mark
	 * @return ResponseEntity indicating the status of the operation
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/mark-attendance")
	@Operation(description = "To mark attendance for students")
	ResponseEntity<String> markAttendance(@RequestBody MarkAttendanceRequestDto requestDto) {
		String result = studentService.markAttendance(requestDto);
		return ResponseEntity.ok(result);
	}

	/**
	 * Endpoint to upload attendance from a CSV file.
	 *
	 * @param date      The date for the attendance
	 * @param className The class name or identifier
	 * @param file      The CSV file containing attendance data
	 * @return ResponseEntity indicating the status of the operation
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/upload-attendance")
	@Operation(description = "To upload attendance from a CSV file")
	ResponseEntity<Object> markAttendanceFromCsv(@RequestParam("date") String date,
			@RequestParam("className") int className, @RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("Please upload a CSV file.");
		}
		String result = studentService.markAttendenceFromCsv(date, className, file);
		return ResponseEntity.ok(result);
	}

	/**
	 * Endpoint to get all students of a particular class.
	 *
	 * @param className The class name or identifier
	 * @return ResponseEntity containing the list of students
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@GetMapping("/all-students/{className}")
	@Operation(description = "To get all students of a particular class")
	ResponseEntity<Object> getAllStudentsByClass(@PathVariable int className) {
		return ResponseHandler.generateResponse("All Students fetched Successfully", HttpStatus.OK,
				studentService.getAllStudents(className));
	}

	/**
	 * Endpoint to publish a notification to students.
	 *
	 * @param message The notification message
	 * @return ResponseEntity indicating the status of the operation
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/publish/notification/{message}")
	@Operation(description = "To publish a notification to students")
	ResponseEntity<Object> publishNotification(@PathVariable String message) {
		return ResponseHandler.generateResponse("Notification published Successfully", HttpStatus.CREATED,
				teacherService.publishNotification(message));
	}
}
