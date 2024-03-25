package com.kidknect.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import com.kidknect.model.User;
import com.kidknect.requestdto.AddStudentsRequestDto;
import com.kidknect.requestdto.MarkAttendanceRequestDto;
import com.kidknect.requestdto.UpdateStudentProfileRequestDto;
import com.kidknect.responsedto.AllStudentsResponseDto;
import com.kidknect.responsedto.AttendenceListResponseDto;
import com.kidknect.responsedto.StudentProfileResponseDto;
import com.kidknect.responsedto.StudentUpdateResponseDto;
import com.kidknect.responsedto.TodayAttendanceResponseDto;

/**
 * Service interface for handling student-related operations.
 */
public interface StudentService {

    /**
     * Find a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return An Optional containing the user if found, or empty if not found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Add students to a specified class.
     *
     * @param className           The class name or identifier.
     * @param addStudentRequestDto The list of student details to add.
     * @return A message indicating the status of the operation.
     */
    String addStudents(int className, List<AddStudentsRequestDto> addStudentRequestDto);

    /**
     * Mark attendance for students.
     *
     * @param requestDto The request object containing attendance details.
     * @return A message indicating the status of the operation.
     */
    String markAttendance(MarkAttendanceRequestDto requestDto);

    /**
     * Update the profile of a student.
     *
     * @param updateProfileRequestDto The request object containing updated profile details.
     * @return The updated student profile response.
     */
    StudentUpdateResponseDto updateStudentProfile(UpdateStudentProfileRequestDto updateProfileRequestDto);

    /**
     * Get the profile of the currently logged-in student.
     *
     * @return The student's profile response.
     */
    StudentProfileResponseDto getProfile();

    /**
     * Get all students belonging to a specified class.
     *
     * @param className The class name or identifier.
     * @return A list of response objects containing details of all students in the class.
     */
    List<AllStudentsResponseDto> getAllStudents(int className);

    /**
     * Add students from a CSV file to a specified class.
     *
     * @param className The class name or identifier.
     * @param file      The CSV file containing student details.
     * @return A message indicating the status of the operation.
     * @throws IOException if an I/O error occurs while processing the file.
     */
    String addStudentsFromCSV(int className, MultipartFile file) throws IOException;

    /**
     * Get attendance list for a specified period.
     *
     * @param startDate The start date of the period.
     * @param endDate   The end date of the period.
     * @return A list of response objects containing attendance details for the specified period.
     */
    List<AttendenceListResponseDto> getAttendenceForMonths(String startDate, String endDate);

    /**
     * Mark attendance from a CSV file for a specified class and date.
     *
     * @param dateStr   The date in string format.
     * @param className The class name or identifier.
     * @param file      The CSV file containing attendance details.
     * @return A message indicating the status of the operation.
     * @throws IOException if an I/O error occurs while processing the file.
     */
    String markAttendenceFromCsv(String dateStr, int className, MultipartFile file);

    /**
     * Get the profile of a student by their roll number.
     *
     * @param rollNo The roll number of the student.
     * @return The student's profile response.
     */
    StudentProfileResponseDto getStudentByRollNo(int rollNo);

    /**
     * Get the attendance details for today for a student.
     *
     * @param rollNo The roll number of the student.
     * @return The today's attendance response for the student.
     */
    TodayAttendanceResponseDto getAttendanceForToday(int rollNo);

}
