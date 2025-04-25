package com.departement.fichedevoeux.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.departement.fichedevoeux.service.FicheDeVoeuxService;

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
        boolean submitted = ficheDeVoeuxService.submitFormulaire(form);
        return submitted ? ResponseEntity.ok("Form submitted")
                         : ResponseEntity.badRequest().body("Invalid form submission");
    }
    
    
    //checks if a professor submitted their fiche
    @GetMapping("/status")
    public ResponseEntity<?> checkStatus(@RequestParam Long professorId) {
        boolean hasSubmitted = ficheDeVoeuxService.hasSubmitted(professorId);
        return ResponseEntity.ok("Status: " + (hasSubmitted ? "Submitted" : "Not submitted yet"));
    }

    //checks if the form is locked
    @GetMapping("/locked")
    public ResponseEntity<?> isFormLocked() {
        boolean locked = ficheDeVoeuxService.isFormLocked();
        return ResponseEntity.ok("Form is currently " + (locked ? "locked" : "unlocked"));
    }

}