package com.departement.fichedevoeux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/export")
    public ResponseEntity<?> exportExcel(@RequestParam Long profId) {
        if (!adminService.isChef(profId)) {
            return ResponseEntity.status(403).body("Access denied: not a department head");
        }
        byte[] fichier = adminService.exporterExcel();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=voeux.xlsx\"").body(fichier);
    }
    //see if the deadline is active
    @GetMapping("/deadline")
    public ResponseEntity<?> getDeadlineStatus() {
        return ResponseEntity.ok("Deadline is active");
    }

    //admin can set/change the deadline
    @PostMapping("/deadline")
    public ResponseEntity<?> setDeadline(@RequestParam Long profId, @RequestBody DeadlineRequestDTO deadlineRequest) {
        if (!isUserChef(profId)) {
            return ResponseEntity.status(403).body("Access denied: not a department head");
        }
        boolean success = adminService.setDeadline(deadlineRequest.getDeadline());
        return success ? ResponseEntity.ok("Deadline updated") : ResponseEntity.badRequest().body("Erreur");
    }

}
