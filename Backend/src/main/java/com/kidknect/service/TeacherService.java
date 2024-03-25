package com.kidknect.service;

import java.util.List;
import java.util.Optional;
import com.kidknect.model.Teacher;
import com.kidknect.requestdto.AddPrincipalRequestDto;
import com.kidknect.requestdto.AddTeachersRequestDto;
import com.kidknect.requestdto.UpdateTeacherProfileRequestDto;
import com.kidknect.responsedto.NotificatonPubishResponseDto;
import com.kidknect.responsedto.TeacheProfileResponseDto;
import com.kidknect.responsedto.TeacherUpdateResponseDto;

/**
 * Service interface for handling teacher-related operations.
 */
public interface TeacherService {

    /**
     * Add a principal to the system.
     *
     * @param addPrincipalRequestDto The request object containing principal details.
     * @return The ID of the added principal.
     */
    Integer addPrincipal(AddPrincipalRequestDto addPrincipalRequestDto);

    /**
     * Find a teacher by their email address.
     *
     * @param email The email address of the teacher to find.
     * @return An Optional containing the teacher if found, or empty if not found.
     */
    Optional<Teacher> findByEmail(String email);

    /**
     * Add multiple teachers to the system.
     *
     * @param addTeachersRequestDto The list of request objects containing teacher details to add.
     * @return A message indicating the status of the operation.
     */
    String addTeachers(List<AddTeachersRequestDto> addTeachersRequestDto);

    /**
     * Update the profile of a teacher.
     *
     * @param updateProfileRequestDto The request object containing updated profile details.
     * @return The updated teacher profile response.
     */
    TeacherUpdateResponseDto updateTeaherProfile(UpdateTeacherProfileRequestDto updateProfileRequestDto);

    /**
     * Get the profile of the currently logged-in teacher.
     *
     * @return The teacher's profile response.
     */
    TeacheProfileResponseDto getProfile();

    /**
     * Publish a notification to all teachers.
     *
     * @param message The message of the notification to publish.
     * @return The response indicating the status of the notification publishing.
     */
    NotificatonPubishResponseDto publishNotification(String message);

}
