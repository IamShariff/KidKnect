package com.kidknect.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kidknect.enums.UserRole;
import com.kidknect.model.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

	Optional<Teacher> findByEmail(String email);

	Optional<Teacher> findByUserRole(UserRole principal);

}
