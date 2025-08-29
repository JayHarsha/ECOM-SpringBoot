package com.ecom.ecommerce.service;

import com.ecom.ecommerce.auth.bean.LoginRequest;
import com.ecom.ecommerce.auth.bean.LoginResponse;
import com.ecom.ecommerce.auth.bean.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Abstraction layer for User Authentication
 */
public interface AuthService {
    LoginResponse signup(SignupRequest request);
    List<SignupRequest> viewAllUsers();
    LoginResponse login(LoginRequest request, HttpServletRequest req, HttpServletResponse res);
    LoginResponse logout(HttpServletRequest req, HttpServletResponse res);
    boolean verifySendEmail(String email);
    LoginResponse changeAuthPassword(SignupRequest request, String otp);
}
