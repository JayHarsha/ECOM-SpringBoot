package com.ecom.ecommerce.controller;

import com.ecom.ecommerce.auth.bean.LoginRequest;
import com.ecom.ecommerce.auth.bean.LoginResponse;
import com.ecom.ecommerce.auth.bean.SignupRequest;
import com.ecom.ecommerce.service.AuthService;
import com.ecom.ecommerce.utility.CommonUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Entry points for the Authentication REST Service
 */
@RestController
@RequestMapping("/ecom/auth")
public class EcomAuthenticateController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CommonUtility util;

    @GetMapping("/")
    public String home() {
        return "Spring Boot is up and running!";
    }

    /**
     * Method to view all users Internal purpose
     *
     * @return
     */
    @GetMapping("/viewAll")
    public ResponseEntity<List<SignupRequest>> viewAllUsers() {
        return ResponseEntity.ok(authService.viewAllUsers());
    }

    /**
     * Method for user to sign-up
     *
     * @param request
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> signup(@Valid @RequestBody SignupRequest request) {
        if (util.isEmpty(request.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse(false, "Bad Request: Missing required field"));
        }
        return ResponseEntity.ok(authService.signup(request));
    }

    /**
     * Method for user to login
     *
     * @param request
     * @param req
     * @param res
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletRequest req, HttpServletResponse res) {
        if (util.isEmpty(request.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse(false, "Bad Request: Missing required field"));
        }
        return ResponseEntity.ok(authService.login(request, req, res));
    }

    /**
     * Method for user to logout
     *
     * @param req
     * @param res
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(HttpServletRequest req, HttpServletResponse res) {
        return ResponseEntity.ok(authService.logout(req, res));
    }

    /**
     * Verify and send OTP to email
     *
     * @param email
     * @param req
     * @param res
     * @return
     */
    @PostMapping("/verify-email")
    public ResponseEntity<LoginResponse> verifySendEmail(@RequestBody String email, HttpServletRequest req, HttpServletResponse res) {
        if (util.isEmpty(email)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse(false, "Bad Request: Missing required field"));
        }
        if (authService.verifySendEmail(email)) {
            return ResponseEntity.ok(new LoginResponse(true, "Email Exists"));
        } else {
            return ResponseEntity.ok(new LoginResponse(false, "Email Doesn't exists"));
        }
    }

    /**
     * Change password by verifying OTP
     *
     * @param request
     * @param otp
     * @param req
     * @param res
     * @return
     */
    @PostMapping("/change-password")
    public ResponseEntity<LoginResponse> changePassword(@RequestBody SignupRequest request, @RequestParam String otp, HttpServletRequest req, HttpServletResponse res) {
        if (util.isEmpty(request.getEmail()) || util.isEmpty(otp) || util.isEmpty(request.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse(false, "Bad Request: Missing required field"));
        }
        return ResponseEntity.ok(authService.changeAuthPassword(request, otp));
    }

}
