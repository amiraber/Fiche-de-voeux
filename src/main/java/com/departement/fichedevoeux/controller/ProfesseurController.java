package com.departement.fichedevoeux.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professeurs")

public class ProfesseurController {
	
	 @GetMapping("/test")
	    public String hello() {
	        return "Professeur Controller is working!";
	    }

}
