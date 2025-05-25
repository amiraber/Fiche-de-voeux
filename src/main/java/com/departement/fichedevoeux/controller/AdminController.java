package com.departement.fichedevoeux.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import com.departement.fichedevoeux.service.AdminService;

import DTO.DeadlineRequestDTO;

@RestController
@RequestMapping("/api/admin")

public class AdminController {

    private final AuthController authController;
	
	private final AdminService adminService;

    public AdminController(AdminService adminService, AuthController authController) {
        this.adminService = adminService;
        this.authController = authController;
    }
    
    @Autowired
    private ProfesseurRepository professeurRepository;

    private boolean isUserChef(Long profId) {
        Professeur prof = professeurRepository.findById(profId).orElse(null);
        return prof != null && prof.isChef(); // or isChef() if using Lombok
    }
    
    //exports data (Fiche de Vœux submissions)
   
  //exports data S1

    @GetMapping("/export/s1")

    public ResponseEntity<?> exportVoeuxS1(@RequestParam Long profId) {

        Professeur prof = professeurRepository.findById(profId).orElse(null);

        if (prof == null || !prof.isChef()) {

            return ResponseEntity.status(403).body("Access denied");

        }



        byte[] fichier = adminService.exporterVoeuxSemestre1(profId);

        return ResponseEntity.ok()

                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=voeux_s1.xlsx")

                .body(fichier);

    }



  //exports data S2

    @GetMapping("/export/s2")

    public ResponseEntity<?> exportVoeuxS2(@RequestParam Long profId) {

        Professeur prof = professeurRepository.findById(profId).orElse(null);

        if (prof == null || !prof.isChef()) {

            return ResponseEntity.status(403).body("Access denied");

        }



        byte[] fichier = adminService.exporterVoeuxSemestre2(profId);

        return ResponseEntity.ok()

                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=voeux_s2.xlsx")

                .body(fichier);

    }

    
    
    
    //see if the deadline is active
    @GetMapping("/deadline")
   
    public ResponseEntity<?> getDeadlineStatus() {
    	 LocalDate deadline = adminService.getDeadline();
    	    if (deadline != null) {
    	        return ResponseEntity.ok("Deadline is active: " + deadline.toString());
    	    } else {
    	        return ResponseEntity.ok("No deadline set");
    	    }
       
    }

    //admin can set/change the deadline
   
    @PostMapping("/deadline")
   
    public ResponseEntity<?> setDeadline(@RequestBody DeadlineRequestDTO deadlineRequest) {
    	
    	Long profId = deadlineRequest.getProfId();
    	
    	
    	if (!isUserChef(profId)) {
            return ResponseEntity.status(403).body("Access denied: not a department head");
        }
    	
        boolean success = adminService.setDeadline(deadlineRequest.getDeadline());
        return success ? ResponseEntity.ok("Deadline updated") : ResponseEntity.badRequest().body("Erreur");
    }

}
