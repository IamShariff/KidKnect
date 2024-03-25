package com.kidknect.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kidknect.model.Attendence;
import com.kidknect.model.Student;

public interface AttendenceRepository extends JpaRepository<Attendence, Long>{

	Optional<List<Attendence>> findByStudentAndDateBetween(Student student, LocalDate start, LocalDate end);

	Optional<Attendence> findByStudentRollNumberAndDate(int rollNo, LocalDate now);
	

}
