package com.kidknect.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.kidknect.enums.AttendenceStatus;
import com.kidknect.enums.UserRole;
import com.kidknect.exception.NotFoundException;
import com.kidknect.model.Attendence;
import com.kidknect.model.Student;
import com.kidknect.model.Teacher;
import com.kidknect.model.User;
import com.kidknect.repository.AttendenceRepository;
import com.kidknect.repository.StudentRepository;
import com.kidknect.repository.TeacherRepository;
import com.kidknect.requestdto.AddStudentsRequestDto;
import com.kidknect.requestdto.MarkAttendanceRequestDto;
import com.kidknect.requestdto.UpdateStudentProfileRequestDto;
import com.kidknect.responsedto.AllStudentsResponseDto;
import com.kidknect.responsedto.AttendenceListResponseDto;
import com.kidknect.responsedto.StudentProfileResponseDto;
import com.kidknect.responsedto.StudentUpdateResponseDto;
import com.kidknect.responsedto.TodayAttendanceResponseDto;
import com.kidknect.service.AuthService;
import com.kidknect.service.JavamailService;
import com.kidknect.service.StudentService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;
	private final TeacherRepository teacherRepository;
	private final JavamailService javamailService;
	private final AuthService authService;
	private final ModelMapper mapper;
	private final AttendenceRepository attendenceRepository;

	@Override
	public Optional<User> findByEmail(String email) {
		return Optional.empty();
	}

	@Override
	public String addStudents(int className, List<AddStudentsRequestDto> addStudentRequestDto) {
		String loginedUserEmail = authService.getLoginedUserEmail();
		Optional<Teacher> findByEmail = teacherRepository.findByEmail(loginedUserEmail);
		List<String> existingEmails = studentRepository.findAll().stream().map(Student::getEmail)
				.collect(Collectors.toList());

		List<String> existingPhoneNumbers = studentRepository.findAll().stream().map(Student::getPhoneNumber)
				.collect(Collectors.toList());

		List<String> newStudents = new ArrayList<>();
		List<String> existingStudents = new ArrayList<>();
		if (findByEmail.isEmpty() || findByEmail.get().getClassName() != className) {
			throw new NotFoundException("Teacher", "Teacher doesn't teach this class");
		}
		for (AddStudentsRequestDto dto : addStudentRequestDto) {
			if (!existingEmails.contains(dto.email()) && !existingPhoneNumbers.contains(dto.phoneNumber())) {
				newStudents.add(dto.name());
				Student student = new Student();
				student.setAdmissionId(dto.admissionId());
				student.setName(dto.name());
				student.setEmail(dto.email());
				student.setPhoneNumber(dto.phoneNumber());
				student.setClassName(className);
				student.setUserRole(UserRole.STUDENT);
				student.setRollNumber(dto.rollNo());
				student.setClassTeacher(findByEmail.get().getName());
				studentRepository.save(student);
			} else {
				existingStudents.add(dto.name());
			}
		}

		String result;
		if (newStudents.isEmpty()) {
			result = "No new Students onboarded.";
		} else if (existingStudents.isEmpty()) {
			result = "New Students onboarded successfully: " + String.join(", ", newStudents);
		} else {
			result = "New Students onboarded: " + String.join(", ", newStudents)
					+ ". Students skipped due to existing email or phone: " + String.join(", ", existingStudents);
		}
		return result;
	}

	@Override
	public String markAttendance(MarkAttendanceRequestDto requestDto) {
		String loginedUserEmail = authService.getLoginedUserEmail();

		Optional<Teacher> optionalTeacher = teacherRepository.findByEmail(loginedUserEmail);
		if (optionalTeacher.isEmpty()) {
			return "Teacher not found";
		}

		Teacher teacher = optionalTeacher.get();
		if (teacher.getClassName() != requestDto.getClassName()) {
			return "Teacher does not teach this class";
		}

		List<MarkAttendanceRequestDto.AttendanceStatusEntry> attendanceStatuses = requestDto.getAttendanceStatuses();
		for (MarkAttendanceRequestDto.AttendanceStatusEntry entry : attendanceStatuses) {
			Optional<Student> optionalStudent = studentRepository.findByRollNumberAndClassName(entry.getRollNo(),
					requestDto.getClassName());
			if (optionalStudent.isPresent()) {
				Student student = optionalStudent.get();
				boolean attendanceExists = student.getAttendances().stream()
						.anyMatch(a -> a.getDate().equals(LocalDate.now()));
				if (attendanceExists) {
					continue;
				}
				AttendenceStatus status = AttendenceStatus.fromString(entry.getAttendanceStatus())
						.orElseThrow(() -> new NotFoundException("Attendance", "Invalid attendance status"));
				Attendence attendance = new Attendence();
				attendance.setStudent(student);
				attendance.setStatus(status);
				if (Objects.equals(status, AttendenceStatus.ABSENT)) {
					sendAbsentNotification(student.getParentGuardianEmail(), LocalDate.now(), student.getName());
				}
				attendance.setDate(LocalDate.now());
				student.addAttendance(attendance);
				studentRepository.save(student);
			}
		}

		return "Attendance marked successfully for all students in class " + requestDto.getClassName();
	}

	@Override
	public StudentUpdateResponseDto updateStudentProfile(UpdateStudentProfileRequestDto updateProfileRequestDto) {
		String loginedUserEmail = authService.getLoginedUserEmail();
		Optional<Student> newlyAddedStudent = studentRepository.findByEmail(loginedUserEmail);
		if (newlyAddedStudent.isEmpty()) {
			throw new NotFoundException("email", "Email not found");
		}
		newlyAddedStudent.get().setAddress(updateProfileRequestDto.address());
		newlyAddedStudent.get().setDateOfBirth(updateProfileRequestDto.dateOfBirth());
		newlyAddedStudent.get().setEmergencyContact(updateProfileRequestDto.emergencyContact());
		newlyAddedStudent.get().setMedicalConditions(updateProfileRequestDto.medicalConditions());
		newlyAddedStudent.get().setParentGuardianEmail(updateProfileRequestDto.parentGuardianEmail());
		newlyAddedStudent.get().setParentGuardianName(updateProfileRequestDto.parentGuardianName());
		Student newStudent = studentRepository.save(newlyAddedStudent.get());
		return mapper.map(newStudent, StudentUpdateResponseDto.class);
	}

	@Override
	public StudentProfileResponseDto getProfile() {
		String loginedUserEmail = authService.getLoginedUserEmail();
		Optional<Student> findByEmail = studentRepository.findByEmail(loginedUserEmail);
		return mapper.map(findByEmail.orElseThrow(() -> new NotFoundException("Student", "Student not found")),
				StudentProfileResponseDto.class);
	}

	@Override
	public List<AllStudentsResponseDto> getAllStudents(int className) {
		List<Student> students = studentRepository.findByClassName(className);

		List<AllStudentsResponseDto> responseDtos = students.stream()
				.map(student -> mapper.map(student, AllStudentsResponseDto.class)).toList();
		return responseDtos;
	}

	@Override
	public String addStudentsFromCSV(int className, MultipartFile file) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			List<AddStudentsRequestDto> studentsToAdd = new ArrayList<>();

			String line = br.readLine();

			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				if (data.length > 1) {
					int admissionId = Integer.parseInt(data[0].trim());
					String name = data[1].trim();
					String email = data[2].trim();
					String phoneNumber = data[3].trim();
					int rollNo = Integer.parseInt(data[4].trim());

					AddStudentsRequestDto student = new AddStudentsRequestDto(admissionId, name, email, phoneNumber,
							rollNo);
					studentsToAdd.add(student);
				} else {
					log.warn("Invalid data format in line: {}", line);
				}
			}
			String result = addStudents(className, studentsToAdd);
			return result;
		} catch (IOException e) {
			log.error("Failed to process CSV file.", e);
			throw new IOException("Failed to process CSV file.", e);
		}
	}

	@Override
	public List<AttendenceListResponseDto> getAttendenceForMonths(String startDate, String endDate) {
		log.info("Getting attendance for dates: {} to {}", startDate, endDate);
		Optional<Student> loginedStudent = studentRepository.findByEmail(authService.getLoginedUserEmail());

		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		Optional<List<Attendence>> attendenceList = attendenceRepository.findByStudentAndDateBetween(
				loginedStudent.orElseThrow(() -> new NotFoundException("Student", "Student not found")), start, end);
		if (attendenceList.isEmpty()) {
			log.warn("Attendance not found");
		}
		List<AttendenceListResponseDto> list = attendenceList.get().stream()
				.map(e -> mapper.map(e, AttendenceListResponseDto.class)).toList();
		return list;

	}

	@Override
	public String markAttendenceFromCsv(String dateStr, int className, MultipartFile file) {
		String loginedUserEmail = authService.getLoginedUserEmail();
		Optional<Teacher> findByEmail = teacherRepository.findByEmail(loginedUserEmail);
		if (findByEmail.isEmpty() || findByEmail.get().getClassName() != className) {
			throw new NotFoundException("Teacher", "Teacher doesn't teach this class");
		}
		try {
			LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			List<Attendence> attendenceList = new ArrayList<>();
			try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
					CSVReader csvReader = new CSVReader(reader)) {
				String[] line;
				csvReader.skip(1);

				while ((line = csvReader.readNext()) != null) {
					int rollNo = Integer.parseInt(line[0]);
					String statusStr = line[1];
					String trimmedStatusStr = statusStr.trim();

					if (trimmedStatusStr.isEmpty()) {
						log.warn("Empty attendance status for roll number: {}", rollNo);
						continue;
					}

					Optional<AttendenceStatus> statusOptional = AttendenceStatus.fromString(trimmedStatusStr);
					if (statusOptional.isPresent()) {
						Attendence attendence = new Attendence();
						attendence.setStatus(statusOptional.get());
						attendence.setDate(date);

						Student student = studentRepository.findByRollNumberAndClassName(rollNo, className)
								.orElse(null);
						if (student != null) {
							attendence.setStudent(student);
							attendenceList.add(attendence);

							// Check if student is absent and send notification to guardian
							if (attendence.getStatus() == AttendenceStatus.ABSENT) {
								sendAbsentNotification(student.getParentGuardianEmail(), date, student.getName());
							}
						} else {
							log.warn("Student with roll number {} not found.", rollNo);
						}
					} else {
						log.warn("Unknown attendance status: {}", trimmedStatusStr);
					}
				}
			} catch (CsvValidationException | NumberFormatException e) {
				log.error("Error processing CSV file.", e);
			}

			attendenceRepository.saveAll(attendenceList);

			return "Attendance marked successfully for " + date;
		} catch (IOException e) {
			log.error("Error processing CSV file.", e);
			return "Error processing CSV file.";
		}
	}

	private void sendAbsentNotification(String guardianEmail, LocalDate date, String studentName) {
		String subject = "Student Absent Notification";
		String text = "Dear Guardian,\n\n" + "This is to inform you that your child, " + studentName
				+ ", was marked as absent on " + date + ".\n" + "Please contact the school for further details.\n\n"
				+ "Sincerely,\nThe School";

		javamailService.sendNotification(guardianEmail, subject, text);
	}

	@Override
	public StudentProfileResponseDto getStudentByRollNo(int rollNo) {
		Student student = studentRepository.findByRollNumber(rollNo)
				.orElseThrow(() -> new NotFoundException("RollNo", "Roll no not found"));
		return mapper.map(student, StudentProfileResponseDto.class);
	}

	@Override
	public TodayAttendanceResponseDto getAttendanceForToday(int rollNo) {
		Attendence todayAttendence = attendenceRepository.findByStudentRollNumberAndDate(rollNo, LocalDate.now())
				.orElseThrow(() -> new NotFoundException("rollNo", "Attendence not marked"));
		return mapper.map(todayAttendence, TodayAttendanceResponseDto.class);
	}
}
