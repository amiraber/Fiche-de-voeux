package com.departement.fichedevoeux.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import DTO.FormulaireRequestDTO;

@RestController
@RequestMapping("/api/form")

public class FicheDeVoeuxController {

	private final FicheDeVoeuxService ficheDeVoeuxService;

    public FicheDeVoeuxController(FicheDeVoeuxService ficheDeVoeuxService) {
        this.ficheDeVoeuxService = ficheDeVoeuxService;
    }
   //handles form submission for course preferences.
    @PostMapping("/submit")
    public ResponseEntity<?> submitForm(@RequestBody FormulaireRequestDTO form) {
        return ResponseEntity.ok("Form submitted");
    }

    //checks if a professor submitted their fiche

    @GetMapping("/status")
    public ResponseEntity<?> checkStatus(@RequestParam Long professorId) {
        return ResponseEntity.ok("Status: Not submitted yet");
    }

    //checks if the form is locked
    @GetMapping("/locked")
    public ResponseEntity<?> isFormLocked() {
        return ResponseEntity.ok("Form is currently unlocked");
    }
	
}
