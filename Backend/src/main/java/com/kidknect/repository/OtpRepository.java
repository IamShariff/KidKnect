package com.kidknect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kidknect.model.Otp;

public interface OtpRepository extends JpaRepository<Otp, Integer> {

	Optional<Otp> findByEmail(String email);

}
