package com.departement.fichedevoeux.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.departement.fichedevoeux.model.Professeur;

import DTO.LoginRequestDTO;
import DTO.RegisterRequestDTO;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

	// This will be injected once AuthService is ready
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        // return authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        // return authService.login(request);
        return ResponseEntity.ok("Login successful");
    }
	
}
