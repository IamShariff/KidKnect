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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.kidknect.exception.NotFoundException;
import com.kidknect.model.Student;
import com.kidknect.model.Teacher;
import com.kidknect.model.TestDetails;
import com.kidknect.repository.StudentRepository;
import com.kidknect.repository.TeacherRepository;
import com.kidknect.repository.TestRepository;
import com.kidknect.requestdto.CsvTestDetailsRequestDto;
import com.kidknect.requestdto.TestDetailsRequestDto;
import com.kidknect.requestdto.TestDetailsRequestDto.TestDetailEntry;
import com.kidknect.responsedto.TestNameResultResponseDto;
import com.kidknect.service.AuthService;
import com.kidknect.service.TestService;
import com.kidknect.util.Constant;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

	private final StudentRepository studentRepository;
	private final TeacherRepository teacherRepository;
	private final TestRepository testRepository;
	private final AuthService authService;
	private final ModelMapper mapper;

	public String addTestDetailsFromCSV(CsvTestDetailsRequestDto csvTestDetailsRequestDto, MultipartFile file) {

		try {
			List<TestDetailEntry> entries = new ArrayList<>();
			try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
					CSVReader csvReader = new CSVReader(reader)) {
				String[] line;
				csvReader.skip(1);

				while ((line = csvReader.readNext()) != null) {

					int rollNo = Integer.parseInt(line[0].trim());
					boolean isGiven = Boolean.parseBoolean(line[1].trim());
					int marksObtained = Integer.parseInt(line[2].trim());
					TestDetailEntry entry = new TestDetailEntry(rollNo, isGiven, marksObtained);
					entries.add(entry);
				}
			} catch (CsvValidationException | NumberFormatException e) {

				System.err.println("Test result is empty");
			}
			return processTestDetails(entries, csvTestDetailsRequestDto);
		} catch (IOException e) {
			return Constant.MESSAGE_TEST_CSV_ERROR;
		}

	}

	private String processTestDetails(List<TestDetailEntry> entries,
			CsvTestDetailsRequestDto csvTestDetailsRequestDto) {
		LocalDate date = LocalDate.parse(csvTestDetailsRequestDto.getTestDate(),
				DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		String email = authService.getLoginedUserEmail();
		Optional<Teacher> optionalTeacher = teacherRepository.findByEmail(email);
		if (optionalTeacher.isEmpty()) {
			throw new NotFoundException(Constant.FIELD_TEACHER, Constant.MESSAGE_TEACHER_NOT_FOUND);
		}

		Teacher teacher = optionalTeacher.get();

		List<String> subjectsForClass = teacher.getSubjectTeaches()
				.get(String.valueOf(csvTestDetailsRequestDto.getClassName()));
		if (subjectsForClass == null || !subjectsForClass.contains(csvTestDetailsRequestDto.getSubject())) {
			throw new NotFoundException(Constant.FIELD_TEACHER, Constant.MESSAGE_TEACHER_NOT_TEACH_CLASS);
		}

		for (TestDetailEntry entry : entries) {
			Optional<Student> optionalStudent = studentRepository.findByRollNumberAndClassName(entry.getRollNumber(),
					csvTestDetailsRequestDto.getClassName());
			if (optionalStudent.isPresent()) {
				TestDetails testDetails = new TestDetails();
				testDetails.setRollNumber(entry.getRollNumber());
				testDetails.setClassName(csvTestDetailsRequestDto.getClassName());
				testDetails.setSubject(csvTestDetailsRequestDto.getSubject());
				testDetails.setTestName(csvTestDetailsRequestDto.getTestName());
				testDetails.setGiven(entry.isGiven());
				testDetails.setTotalMarks(csvTestDetailsRequestDto.getTotalMarks());
				testDetails.setMarksObtained(entry.getMarksObtained());
				testDetails.setTestDate(date);
				testRepository.save(testDetails);
			}
		}

		return Constant.MESSAGE_TEST_DETAILS_ADDED_SUCCESS;
	}

	@Override
	public String addTestDetails(TestDetailsRequestDto requestDto) {
		LocalDate date = LocalDate.parse(requestDto.getTestDate(),
				DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		String email = authService.getLoginedUserEmail();
		Optional<Teacher> optionalTeacher = teacherRepository.findByEmail(email);
		if (optionalTeacher.isEmpty()) {
			throw new NotFoundException(Constant.FIELD_TEACHER, Constant.MESSAGE_TEACHER_NOT_FOUND);
		}

		Teacher teacher = optionalTeacher.get();
		Integer className = requestDto.getClassName();
		String subject = requestDto.getSubject();
		List<String> subjectsForClass = teacher.getSubjectTeaches().get(String.valueOf(className)); // Convert to String
		if (subjectsForClass == null || !subjectsForClass.contains(subject)) {
			throw new NotFoundException(Constant.FIELD_TEACHER, Constant.MESSAGE_TEACHER_NOT_TEACH_CLASS);
		}
		for (TestDetailEntry entry : requestDto.getTestDetails()) {
			Optional<Student> optionalStudent = studentRepository.findByRollNumberAndClassName(entry.getRollNumber(),
					className);
			if (optionalStudent.isPresent()) {
				TestDetails testDetails = new TestDetails();
				testDetails.setRollNumber(entry.getRollNumber());
				testDetails.setClassName(className);
				testDetails.setSubject(subject);
				testDetails.setTestName(requestDto.getTestName());
				testDetails.setTotalMarks(requestDto.getTotalMarks());
				testDetails.setTestDate(date);
				testDetails.setGiven(entry.isGiven());
				testDetails.setMarksObtained(entry.getMarksObtained());

				testRepository.save(testDetails);

			}
		}
		return Constant.MESSAGE_TEST_DETAILS_ADDED_SUCCESS;
	}

	@Override
	public List<TestNameResultResponseDto> getResultForTestName(String testName, String subject) {
		String loginedUserEmail = authService.getLoginedUserEmail();
		Student student = studentRepository.findByEmail(loginedUserEmail).get();
		if (Objects.nonNull(subject)) {
			List<TestDetails> findByTestNameAndSubjectAndEmail = testRepository
					.findByTestNameAndSubjectAndRollNumber(testName, subject, student.getRollNumber());

			return findByTestNameAndSubjectAndEmail.stream()
					.map(result -> mapper.map(result, TestNameResultResponseDto.class)).toList();
		} else {
			List<TestDetails> findByTestNameAndRollNumber = testRepository.findByTestNameAndRollNumber(testName,
					student.getRollNumber());
			return findByTestNameAndRollNumber.stream()
					.map(result -> mapper.map(result, TestNameResultResponseDto.class)).toList();
		}
	}
}
