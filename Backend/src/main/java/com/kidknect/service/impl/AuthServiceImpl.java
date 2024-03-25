package com.kidknect.service.impl;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.kidknect.exception.NotFoundException;
import com.kidknect.model.Student;
import com.kidknect.model.Teacher;
import com.kidknect.repository.StudentRepository;
import com.kidknect.repository.TeacherRepository;
import com.kidknect.requestdto.EmailOtpRequestDto;
import com.kidknect.responsedto.EmailOtpResponseDto;
import com.kidknect.service.AuthService;
import com.kidknect.service.OtpService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final StudentRepository studentRepository;
	private final TeacherRepository teacherRepository;
	private final PasswordEncoder passwordEncoder;
	private final OtpService otpService;
	
	
	public String getLoginedUserEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}


	@Override
	public EmailOtpResponseDto forgetPassword(String email) {
		Optional<Teacher> teacherOptional = teacherRepository.findByEmail(email);
		Optional<Student> studentOptional = studentRepository.findByEmail(email);

		EmailOtpRequestDto requestDto = new EmailOtpRequestDto(email, "Password Reset");

		EmailOtpResponseDto responseDto = Stream.of(teacherOptional, studentOptional).filter(Optional::isPresent)
				.findFirst().map(optional -> otpService.sendOtpToEmail(requestDto))
				.orElseThrow(() -> new NotFoundException("Email not found: ", email));

		return responseDto;
	}

	@Override
	public Boolean verifyOtp(String email, Integer Otp) {
		return otpService.isOtpValid(email, Otp);
	}

	@Override
	public Boolean resetPassword(String email, String password) {
		Optional<Teacher> teacherOptional = teacherRepository.findByEmail(email);
		teacherOptional.ifPresent(teacher -> {
			teacher.setPassword(passwordEncoder.encode(password));
			teacherRepository.save(teacher);
		});

		Optional<Student> studentOptional = studentRepository.findByEmail(email);
		studentOptional.ifPresent(student -> {
			student.setPassword(passwordEncoder.encode(password));
			studentRepository.save(student);
		});

		if (!teacherOptional.isPresent() && !studentOptional.isPresent()) {
			throw new NotFoundException("Email not found: ", email);
		}

		return true;
	}

}
