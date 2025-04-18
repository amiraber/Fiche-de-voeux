package com.departement.fichedevoeux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import DTO.DeadlineRequestDTO;

@RestController
@RequestMapping("/api/admin")

public class AdminController {
	
	private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    //exports data (Fiche de Vœux submissions)
    @GetMapping("/export")
    public ResponseEntity<?> exportExcel() {
        return ResponseEntity.ok("Excel exported");
    }
    //see if the deadline is active
    @GetMapping("/deadline")
    public ResponseEntity<?> getDeadlineStatus() {
        return ResponseEntity.ok("Deadline is active");
    }

    //admin can set/change the deadline
    @PostMapping("/deadline")
    public ResponseEntity<?> setDeadline(@RequestBody DeadlineRequestDTO deadlineRequest) {
        return ResponseEntity.ok("Deadline updated");
    }

}
