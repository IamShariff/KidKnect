package com.kidknect.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.kidknect.exception.NotFoundException;
import com.kidknect.model.Student;
import com.kidknect.model.Teacher;
import com.kidknect.model.User;
import com.kidknect.repository.StudentRepository;
import com.kidknect.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

/**
 * Custom implementation of Spring Security's UserDetailsService. This service
 * is responsible for loading user details from the database.
 */
@Component
@RequiredArgsConstructor
public class SpringUserDetailsService implements UserDetailsService {

	private final StudentRepository studentRepository;
	private final TeacherRepository teacherRepository;

	/**
	 * Loads user details by username (email) from the database.
	 *
	 * @param email The email (username) of the user
	 * @return UserDetails object representing the user
	 * @throws UsernameNotFoundException If the user is not found
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Student> studentOptional = studentRepository.findByEmail(email);
		if (studentOptional.isPresent()) {
			return mapToUserDetails(studentOptional.get());
		} else {
			Optional<Teacher> teacherOptional = teacherRepository.findByEmail(email);
			if (teacherOptional.isPresent()) {
				return mapToUserDetails(teacherOptional.get());
			} else {
				// User with the given email not found
				throw new NotFoundException("ExceptionConstants.USER_DONT_EXIST", email);
			}
		}
	}

	/**
	 * Maps a User entity to UserDetails.
	 *
	 * @param user The User entity to map
	 * @return UserDetails object representing the user
	 */
	private UserDetails mapToUserDetails(User user) {
		if (user instanceof Teacher) {
			return new SpringUserDetails((Teacher) user);
		} else if (user instanceof Student) {
			return new SpringUserDetails((Student) user);
		} else {
			// Unsupported user type
			throw new IllegalArgumentException("Unsupported user type: " + user.getClass());
		}
	}
}
