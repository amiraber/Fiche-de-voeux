package com.departement.fichedevoeux.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.departement.fichedevoeux.model.Voeux;
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
    
    
    
    // pour la modification, sans qu'il ressaisit à 0 
    @GetMapping("/get")
    public ResponseEntity<?> getFiche(@RequestParam Long professeurId){
    	List<Voeux> voeux = ficheDeVoeuxService.getVoeuxDuProf(professeurId);
    	return ResponseEntity.ok(voeux);
    }

    // dès qu'il coche la case de remplir les choix de l'annee precedente, le frontend les recupere du backend et les affiche (rempli le formulaire)
    @PutMapping("/formulaire-choix")
    public ResponseEntity<?> preRemplirFormulaireAnneePrecedente(@RequestParam Long professeurId, @RequestParam boolean copierAnneePrecedente) {
        List<Voeux> choixAnneePrecedente = null;

        if (copierAnneePrecedente) {
            choixAnneePrecedente = ficheDeVoeuxService.getChoixAnneePrecedente(professeurId);
        }
        // Retourner les choix pour le frontend
        return ResponseEntity.ok(choixAnneePrecedente);
    }
}