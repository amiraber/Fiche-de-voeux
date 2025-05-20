package com.departement.fichedevoeux.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import com.departement.fichedevoeux.service.AuthService;

import DTO.LoginRequestDTO;
import DTO.RegisterRequestDTO;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;   // or javax.servlet.http.HttpServletRequest on Boot ≤ 2.7


@RestController
@RequestMapping("/api/auth")

public class AuthController {
	
	//@Autowired AuthenticationManager authenticationManager;
	
	@Autowired ProfesseurRepository professeurRepository;
	
	private static final Logger log = LoggerFactory.getLogger(AuthService.class);

	// This will be injected once AuthService is ready
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
       // this.authenticationManager = authenticationManager;
        
    }
    
    

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
    	log.info(">>> /register called with {}", request.getEmail());
    	boolean success = authService.register(request);
    	return success ? ResponseEntity.ok("User registered successfully") : ResponseEntity.badRequest().body("Email already used");
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
    	
    	log.info(">>> /login called with {}", request.getEmail());

        boolean valid = authService.login(request);
        if (!valid) {
            return ResponseEntity.status(401).body("Invalid Email or password");
        }

        // Fetch Professeur by email
        Professeur prof = professeurRepository.findByEmail(request.getEmail());

        if (prof == null) {
            // This should rarely happen if login was successful,
            // but just in case:
            return ResponseEntity.status(404).body("Professeur not found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("professeurId", prof.getId());

        return ResponseEntity.ok(response);
    }
	
	
}
