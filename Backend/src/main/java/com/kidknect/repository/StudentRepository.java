package com.kidknect.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kidknect.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	Optional<Student> findByEmail(String email);

	Optional<Student> findByNameAndClassName(String studentName, int className);

	Optional<Student> findByRollNumberAndClassName(int rollNo, int className);

	List<Student> findByClassName(int className);

	Optional<Student> findByRollNumber(int rollNo);

}
