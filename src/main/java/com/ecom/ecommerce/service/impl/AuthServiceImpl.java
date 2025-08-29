package com.ecom.ecommerce.service.impl;

import com.ecom.ecommerce.auth.bean.LoginRequest;
import com.ecom.ecommerce.auth.bean.LoginResponse;
import com.ecom.ecommerce.auth.bean.SignupRequest;
import com.ecom.ecommerce.dao.OtpRepository;
import com.ecom.ecommerce.dao.UserRepository;
import com.ecom.ecommerce.entity.Otp;
import com.ecom.ecommerce.entity.Users;
import com.ecom.ecommerce.service.AuthService;
import com.ecom.ecommerce.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public LoginResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new LoginResponse(false, "Email already registered.");
        }
        Users user = new Users();
        user.setUserName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        return new LoginResponse(true, "User registered successfully.");
    }

    @Override
    public List<SignupRequest> viewAllUsers() {
        List<Users> allUsers = userRepository.findAll();
        return allUsers.stream().map(users -> new SignupRequest(users.getUserName(), users.getEmail(), users.getPasswordHash())).collect(Collectors.toList());
    }

    @Override
    public LoginResponse login(LoginRequest request, HttpServletRequest req, HttpServletResponse res) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            Authentication auth = authenticationManager.authenticate(authToken);

            // If authentication fails, an exception is thrown
            return new LoginResponse(true, "Login successful.");
        } catch (BadCredentialsException e) {
            return new LoginResponse(false, "Incorrect password.");
        } catch (UsernameNotFoundException e) {
            return new LoginResponse(false, "User not found.");
        } catch (AuthenticationException e) {
            return new LoginResponse(false, "Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public LoginResponse logout(HttpServletRequest req, HttpServletResponse res) {
        new SecurityContextLogoutHandler().logout(req, res, null);
        return new LoginResponse(true, "Logout successful.");
    }

    @Override
    @Transactional
    public boolean verifySendEmail(String email) {
        Users user = userRepository.findByEmail(email)
                .orElse(null);
        if(user != null){
            String otp = String.format("%06d", new Random().nextInt(999999));
            Otp otpEntity = new Otp();
            otpEntity.setUserId(user.getUserId());
            otpEntity.setOtp(otp);
            otpEntity.setCreatedAt(LocalDateTime.now());
            otpEntity.setExpiredAt(LocalDateTime.now().plusMinutes(5));
            otpEntity.setEmail(email);
            otpRepository.save(otpEntity);
            emailService.sendOtpToEmail(user.getEmail(), otp);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public LoginResponse changeAuthPassword(SignupRequest request, String otp) {
        Users userEntity = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (userEntity == null) {
            return new LoginResponse(false, "Email is not registered. Please sign-up!");
        }
        Otp otpEntity = otpRepository.findByEmail(request.getEmail()).orElse(null);
        if(otpEntity ==  null){
            return new LoginResponse(false, "OTP not found. Please request a new OTP.");
        } else if (otpEntity.getExpiredAt().isBefore(LocalDateTime.now())) {
            return new LoginResponse(false, "OTP has expired. Please request a new OTP.");
        }

        if(otp.equals(otpEntity.getOtp())){
            userEntity.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            userRepository.save(userEntity);
            return new LoginResponse(true, "Password updated successfully");
        }else{
            return new LoginResponse(false, "Incorrect OTP. Please enter Valid OTP...");
        }

    }
}
