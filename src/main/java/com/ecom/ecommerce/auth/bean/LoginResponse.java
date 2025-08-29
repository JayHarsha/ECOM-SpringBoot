package com.ecom.ecommerce.auth.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response POJO for User Login
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
}
