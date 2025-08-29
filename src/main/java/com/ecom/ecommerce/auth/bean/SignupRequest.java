package com.ecom.ecommerce.auth.bean;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Request POJO for User Signup
 */
@Data
@AllArgsConstructor
public class SignupRequest {
    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
