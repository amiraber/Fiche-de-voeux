package com.departement.fichedevoeux.controller;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import com.departement.fichedevoeux.service.ProfesseurService;

import DTO.ProfesseurDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professeurs")

public class ProfesseurController {

    @Autowired
    private ProfesseurService professeurService;

    // Tous les professeurs (en DTO)
    @GetMapping
    public List<ProfesseurDTO> getAll() {
        return professeurService.getAll();
    }

    // Prof par ID
    @GetMapping("/{id}")
    public ProfesseurDTO getById(@PathVariable Long id) {
        return professeurService.getById(id);
    }

    // Par email
    @GetMapping("/by-email")
    public ProfesseurDTO getByEmail(@RequestParam String email) {
        return professeurService.getByEmail(email);
    }

    // Tous les profs d’un département
    @GetMapping("/by-departement/{id}")
    public List<ProfesseurDTO> getByDepartment(@PathVariable Long id) {
        return professeurService.getByDepartement(id);
    }

    // Tous les chefs
    @GetMapping("/chef")
    public List<ProfesseurDTO> getChefs() {
        return professeurService.getChefs();
    }

    // Créer un prof (retourne success boolean ou message)
    @PostMapping
    public String create(@RequestBody Professeur prof) {
        boolean success = professeurService.createProfesseur(prof);
        return success ? "Professeur créé avec succès" : "Email déjà utilisé";
    }

    // Test API
    @GetMapping("/test")
    public String hello() {
        return "Professeur Controller is working!";
    }
}



