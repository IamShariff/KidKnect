package com.kidknect.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.kidknect.enums.UserRole;
import com.kidknect.exception.AlreadyExistException;
import com.kidknect.exception.NotFoundException;
import com.kidknect.model.Notification;
import com.kidknect.model.Teacher;
import com.kidknect.repository.NotificationRepository;
import com.kidknect.repository.TeacherRepository;
import com.kidknect.requestdto.AddPrincipalRequestDto;
import com.kidknect.requestdto.AddTeachersRequestDto;
import com.kidknect.requestdto.UpdateTeacherProfileRequestDto;
import com.kidknect.responsedto.NotificatonPubishResponseDto;
import com.kidknect.responsedto.TeacheProfileResponseDto;
import com.kidknect.responsedto.TeacherUpdateResponseDto;
import com.kidknect.service.AuthService;
import com.kidknect.service.TeacherService;
import com.kidknect.util.Constant;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

	private final TeacherRepository teacherRepository;
	private final ModelMapper mapper;
	private final PasswordEncoder passwordEncoder;
	private final AuthService authService;
	private final NotificationRepository notificationRepository;

	@Override
	public Integer addPrincipal(AddPrincipalRequestDto addPrincipalRequestDto) {
		if (teacherRepository.findByUserRole(UserRole.PRINCIPAL).isPresent()) {
			throw new AlreadyExistException(Constant.FIELD_PRINCIPAL, Constant.PRINCIPAL_ALREADY_EXIST);
		}

		Teacher principal = new Teacher();
		principal.setName(addPrincipalRequestDto.name());
		principal.setEmail(addPrincipalRequestDto.email());
		principal.setPassword(passwordEncoder.encode(addPrincipalRequestDto.password()));
		principal.setPhoneNumber(addPrincipalRequestDto.phoneNumber());
		principal.setUserRole(UserRole.PRINCIPAL);
		Teacher newlyAddedPrincipal = teacherRepository.save(principal);
		return newlyAddedPrincipal.getUserId();
	}

	@Override
	public Optional<Teacher> findByEmail(String email) {
		return teacherRepository.findByEmail(email);
	}

	@Override
	public String addTeachers(List<AddTeachersRequestDto> addTeachersRequestDto) {
		List<String> existingEmails = teacherRepository.findAll().stream().map(Teacher::getEmail)
				.collect(Collectors.toList());

		List<String> existingPhoneNumbers = teacherRepository.findAll().stream().map(Teacher::getPhoneNumber)
				.collect(Collectors.toList());

		List<String> newTeachers = new ArrayList<>();
		List<String> existingTeachers = new ArrayList<>();

		for (AddTeachersRequestDto dto : addTeachersRequestDto) {
			if (!existingEmails.contains(dto.email()) && !existingPhoneNumbers.contains(dto.phoneNumber())) {
				newTeachers.add(dto.name());
				Teacher teacher = new Teacher();
				teacher.setName(dto.name());
				teacher.setEmail(dto.email());
				teacher.setPhoneNumber(dto.phoneNumber());
				teacher.setUserRole(UserRole.TEACHER);
				teacherRepository.save(teacher);
			} else {
				existingTeachers.add(dto.name());
			}
		}
		String result;
		if (newTeachers.isEmpty()) {
			result = Constant.MESSAGE_TEACHER_NOT_FOUND;
		} else if (existingTeachers.isEmpty()) {
			result = Constant.TEACHERS_ONBOARDED_SUCCESSFULLY + String.join(", ", newTeachers);
		} else {
			result = Constant.TEACHERS_ONBOARDED + String.join(", ", newTeachers) + Constant.TEACHERS_SKIPPED
					+ String.join(", ", existingTeachers);
		}
		return result;
	}

	@Override
	public TeacherUpdateResponseDto updateTeaherProfile(UpdateTeacherProfileRequestDto updateProfileRequestDto) {
		String email = authService.getLoginedUserEmail();
		Optional<Teacher> optionalTeacher = teacherRepository.findByEmail(email);
		if (optionalTeacher.isEmpty()) {
			throw new NotFoundException(Constant.FIELD_EMAIL, Constant.MESSAGE_TEACHER_NOT_FOUND);
		}
		Teacher teacher = optionalTeacher.get();
		teacher.setClassTeacher(updateProfileRequestDto.isClassTeacher());
		teacher.setClassName(updateProfileRequestDto.className());
		teacher.setSubjectTeaches(updateProfileRequestDto.subjectTeaches());
		teacher.setAddress(updateProfileRequestDto.address());
		teacher.setQualifications(updateProfileRequestDto.qualifications());
		teacher.setCertifications(updateProfileRequestDto.certifications());
		teacher.setTeachingExperience(updateProfileRequestDto.teachingExperience());
		teacher.setResponsibilities(updateProfileRequestDto.responsibilities());
		Teacher updatedTeacher = teacherRepository.save(teacher);
		return mapper.map(updatedTeacher, TeacherUpdateResponseDto.class);
	}

	@Override
	public TeacheProfileResponseDto getProfile() {
		Teacher teacher = teacherRepository.findByEmail(authService.getLoginedUserEmail())
				.orElseThrow(() -> new NotFoundException(Constant.FIELD_TEACHER, Constant.MESSAGE_TEACHER_NOT_FOUND));
		return mapper.map(teacher, TeacheProfileResponseDto.class);
	}

	@Override
	public NotificatonPubishResponseDto publishNotification(String message) {
		String loginedUserEmail = authService.getLoginedUserEmail();
		Notification newNotification = Notification.builder().message(message).publishDate(LocalDate.now())
				.publishedBy(loginedUserEmail).build();
		Notification newlyAddedNotification = notificationRepository.save(newNotification);
		return mapper.map(newlyAddedNotification, NotificatonPubishResponseDto.class);
	}
}
