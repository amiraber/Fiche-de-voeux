package com.departement.fichedevoeux.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.service.AuthService;

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
    	boolean success = authService.register(request);
    	return success ? ResponseEntity.ok("User registered successfully") : ResponseEntity.badRequest().body("Email already used");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
    	boolean valid = authService.login(request);
    	return valid ? ResponseEntity.ok("Login successful") : ResponseEntity.status(401).body("Invalid Email or password");
    }
	
}
