package com.ecom.ecommerce.dao;

import com.ecom.ecommerce.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Abstraction layer for OTP Repo
 */
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByEmail(String email);
}
