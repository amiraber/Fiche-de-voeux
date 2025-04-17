package com.departement.fichedevoeux.controller;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professeurs")


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professeurs")

public class ProfesseurController {

	@Autowired
    private ProfesseurRepository professeurRepository;
    @GetMapping
    public List<Professeur> getAll() {
        return professeurRepository.findAll();
    }
    @GetMapping("/{id}")
    public Professeur getById(@PathVariable Long id) {
        return professeurRepository.findById(id).orElse(null);
    }
    @GetMapping("/by-email")
    public Professeur getByEmail(@RequestParam String email) {
        return professeurRepository.findByEmail(email);
    }
    @GetMapping("/by-departement/{id}")
    public List<Professeur> getByDepartment(@PathVariable Long id) {
        return professeurRepository.findByDepartementId(id);
    }
    @GetMapping("/chef")
    public List<Professeur> getChefs() {
        return professeurRepository.findByIsChefTrue();
    }
    @PostMapping
    public Professeur create(@RequestBody Professeur prof) {
        return professeurRepository.save(prof);
    }

	
	 @GetMapping("/test")
	    public String hello() {
	        return "Professeur Controller is working!";
	    }

}



