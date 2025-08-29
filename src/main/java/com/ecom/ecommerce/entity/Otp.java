package com.ecom.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entity for OTP table
 */
@Entity
@Data
@Table(name = "otp", schema = "ecom")
public class Otp {
    @Id
    @Column(name = "user_id")
    private Long userId;  // Same as userId from Users table (Primary Key and Foreign Key)

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId  // This is the key annotation: binds PK and FK
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users user;

    @Column(nullable = false, length = 6)
    private String otp;  // OTP value (6 digits or whatever format you want)

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private String email;

}
