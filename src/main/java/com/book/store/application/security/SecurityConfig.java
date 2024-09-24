package com.book.store.application.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Disable CSRF protection for simplicity
            .authorizeHttpRequests()
            .requestMatchers("/api/v1/users/otpVerification", "/api/v1/customers/register", "/api/v1/sellers", "/api/v1/admin/register").permitAll()  // Allow public access to these endpoints
            .anyRequest().authenticated()  // All other requests require authentication
            .and()
            .httpBasic();  // Use basic authentication
        return http.build();
    }
}
