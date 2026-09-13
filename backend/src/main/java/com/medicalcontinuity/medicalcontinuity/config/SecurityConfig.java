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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/unknown-patients/**").permitAll()
                        .requestMatchers("/api/ai/health").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // PATIENT - can manage own profile and upload own documents
                        .requestMatchers(HttpMethod.POST, "/api/patients").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/patients/**").hasAnyRole("PATIENT", "DOCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/patients/**").hasAnyRole("PATIENT", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/patients/**").hasAnyRole("PATIENT", "DOCTOR", "NURSE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/medical-documents/**").hasAnyRole("PATIENT", "DOCTOR", "NURSE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/medical-documents/**").hasAnyRole("PATIENT", "DOCTOR", "NURSE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/medical-records/**").hasAnyRole("PATIENT", "DOCTOR", "NURSE", "ADMIN")

                        // DOCTOR and NURSE can manage records and encounters
                        .requestMatchers(HttpMethod.POST, "/api/encounters").hasAnyRole("DOCTOR", "NURSE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/medical-records").hasAnyRole("DOCTOR", "ADMIN")
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
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
