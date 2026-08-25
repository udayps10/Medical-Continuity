package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.dto.*;
import com.medicalcontinuity.medicalcontinuity.entity.User;
import com.medicalcontinuity.medicalcontinuity.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestBody SendOtpRequest req) {
        String otp = authService.sendOtp(req.getEmail());
        return ResponseEntity.ok(Map.of("message", "OTP sent", "otp", otp));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(@RequestBody VerifyOtpRequest req) {
        return ResponseEntity.ok(authService.verifyOtp(req));
    }
}
