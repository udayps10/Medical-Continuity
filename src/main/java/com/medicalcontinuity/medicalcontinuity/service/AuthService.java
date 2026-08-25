package com.medicalcontinuity.medicalcontinuity.service;

import com.medicalcontinuity.medicalcontinuity.dto.*;
import com.medicalcontinuity.medicalcontinuity.entity.*;
import com.medicalcontinuity.medicalcontinuity.repositories.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Value("${jwt.secret:medicalcontinuitysecretkey12345678901234567890}")
    private String jwtSecret;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public User register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User(req.getName(), req.getEmail(), User.Role.valueOf(req.getRole().toUpperCase()));
        return userRepository.save(user);
    }

    public String sendOtp(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);
        return otp;
    }

    public AuthResponse verifyOtp(VerifyOtpRequest req) {
        User user = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getOtp() == null || user.getOtpExpiry() == null) {
            throw new RuntimeException("No OTP sent");
        }
        if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            throw new RuntimeException("OTP expired");
        }
        if (!user.getOtp().equals(req.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }
        user.setOtpVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        String token = generateToken(user);
        return new AuthResponse(token, user.getId(), user.getRole().name());
    }

    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Long.parseLong(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject());
    }
}
