package com.medicalcontinuity.medicalcontinuity.config;

import com.medicalcontinuity.medicalcontinuity.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/unknown-patients/**").permitAll()
                        .requestMatchers("/api/ai/health").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // PATIENT only - can create their own profile
                        .requestMatchers(HttpMethod.POST, "/api/patients").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/patients/**").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/patients/**").hasRole("PATIENT")

                        // DOCTOR and NURSE can view patients and manage records
                        .requestMatchers(HttpMethod.GET, "/api/patients/**").hasAnyRole("DOCTOR", "NURSE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/encounters").hasAnyRole("DOCTOR", "NURSE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/medical-records").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/medical-documents/**").hasAnyRole("DOCTOR", "NURSE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/patient-matches").hasAnyRole("DOCTOR", "ADMIN")

                        // ADMIN only
                        .requestMatchers("/api/hospitals/**").hasRole("ADMIN")
                        .requestMatchers("/api/ai/match/**").hasAnyRole("DOCTOR", "ADMIN")

                        // Default
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
