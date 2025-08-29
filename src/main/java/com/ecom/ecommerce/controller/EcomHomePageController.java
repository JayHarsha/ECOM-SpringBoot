package com.ecom.ecommerce.controller;

import com.ecom.ecommerce.auth.bean.SignupRequest;
import com.ecom.ecommerce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ecom/auth")
public class EcomHomePageController {
    @Autowired
    private AuthService authService;

    @GetMapping("/fetchAvailableData")
    public ResponseEntity<List<SignupRequest>> fetchAvailableData(){
        return ResponseEntity.ok(authService.viewAllUsers());
    }
}
