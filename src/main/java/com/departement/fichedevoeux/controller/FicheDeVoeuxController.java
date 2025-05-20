package com.departement.fichedevoeux.controller;

import com.departement.fichedevoeux.model.Module;
import com.departement.fichedevoeux.model.Voeux;
import com.departement.fichedevoeux.service.AuthService;
import com.departement.fichedevoeux.service.FicheDeVoeuxService;
import DTO.FormulaireRequestDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/form")
public class FicheDeVoeuxController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthService.class);


    private final FicheDeVoeuxService ficheDeVoeuxService;

    public FicheDeVoeuxController(FicheDeVoeuxService ficheDeVoeuxService) {
        this.ficheDeVoeuxService = ficheDeVoeuxService;
    }

    // 1. Soumettre la fiche de vœux
    @PostMapping("/submit")
    //@CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity<?> submitForm(@RequestBody FormulaireRequestDTO form) {
    	
    	log.info(">>> /SUBMIT called with {}", form);  
    	
        boolean submitted = ficheDeVoeuxService.submitFormulaire(form);
        return submitted
                ? ResponseEntity.ok("Formulaire soumis avec succès.")
                : ResponseEntity.badRequest().body("Échec de la soumission du formulaire.");
    }

    // 2. Vérifier si le professeur a déjà soumis
    @GetMapping("/status")
    public ResponseEntity<String> checkStatus(@RequestParam Long professeurId) {
    	
    	log.info(">>> /Status called with {}", professeurId); 
    	
        boolean hasSubmitted = ficheDeVoeuxService.hasSubmitted(professeurId);
        return ResponseEntity.ok("Statut : " + (hasSubmitted ? "Soumis" : "Non soumis"));
    }

    // 3. Vérifier si le formulaire est verrouillé
    @GetMapping("/locked")
    public ResponseEntity<String> isFormLocked() {
        boolean locked = ficheDeVoeuxService.isFormLocked();
        return ResponseEntity.ok("Formulaire actuellement " + (locked ? "verrouillé" : "déverrouillé"));
    }

    // 4. Récupérer les choix du professeur (modification)
    @GetMapping("/get")
    public ResponseEntity<List<Voeux>> getFiche(@RequestParam Long professeurId) {
        return ResponseEntity.ok(ficheDeVoeuxService.getVoeuxDuProf(professeurId));
    }

    // 5. Préremplir avec les choix de l'année précédente
    @PutMapping("/formulaire-choix")
    public ResponseEntity<List<Voeux>> preRemplirFormulaireAnneePrecedente(
            @RequestParam Long professeurId,
            @RequestParam boolean copierAnneePrecedente) {

        List<Voeux> anciensChoix = copierAnneePrecedente
                ? ficheDeVoeuxService.getChoixAnneePrecedente(professeurId)
                : List.of();

        return ResponseEntity.ok(anciensChoix);
    }

    // 6. Filtrer les modules selon semestre + pallier + spécialité
    @GetMapping("/modules")
    public ResponseEntity<List<Module>> getModulesFiltres(
            @RequestParam String semestre,
            @RequestParam String pallier,
            @RequestParam String specialite) {

        return ResponseEntity.ok(ficheDeVoeuxService.filterModules(semestre, pallier, specialite));
    }

    // 7. Obtenir la liste des spécialités disponibles pour un semestre et un pallier
    @GetMapping("/specialites")
    public ResponseEntity<List<String>> getSpecialitesFiltres(
            @RequestParam String semestre,
            @RequestParam String pallier) {

        return ResponseEntity.ok(ficheDeVoeuxService.filterSpecialites(semestre, pallier));
    }
}
