package com.medicalcontinuity.medicalcontinuity.controller;

import com.medicalcontinuity.medicalcontinuity.entity.User;
import com.medicalcontinuity.medicalcontinuity.repository.UserRepository;
import com.medicalcontinuity.medicalcontinuity.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Register + login. Issues JWTs via the existing JwtUtil so tokens
 * are readable by the existing JwtAuthFilter without any change to
 * that filter.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // POST /api/auth/register
    // Body: full User JSON (email, password, fullName, role required; leave id blank)
    // Calls: UserRepository.existsByEmail() -> reject duplicate emails
    //        PasswordEncoder.encode()       -> hash the raw password before storing
    //        UserRepository.save()          -> persist the new user
    //        JwtUtil.generateToken()        -> issue a token immediately on signup
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered: " + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        User saved = userRepository.save(user);

        String token = jwtUtil.generateToken(toSpringUser(saved));
        return ResponseEntity.ok(authResponse(token, saved));
    }

    // POST /api/auth/login
    // Body: { "email": "...", "password": "..." }
    // Calls: UserRepository.findByEmail() -> look up the account
    //        PasswordEncoder.matches()    -> verify raw password against stored hash
    //        JwtUtil.generateToken()      -> issue a token on successful login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String rawPassword = body.get("password");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        if (!user.isEnabled()) {
            return ResponseEntity.status(403).body("Account is disabled");
        }

        String token = jwtUtil.generateToken(toSpringUser(user));
        return ResponseEntity.ok(authResponse(token, user));
    }

    private Map<String, Object> authResponse(String token, User user) {
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());
        response.put("email", user.getEmail());
        response.put("fullName", user.getFullName());
        response.put("role", user.getRole().name());
        return response;
    }

    /**
     * JwtUtil.generateToken() takes a Spring Security UserDetails,
     * matching how JwtAuthFilter builds it when validating incoming
     * tokens — kept identical here so tokens are generated the same
     * way they're later verified.
     */
    private org.springframework.security.core.userdetails.User toSpringUser(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(List.of(
                        new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name())))
                .build();
    }
}