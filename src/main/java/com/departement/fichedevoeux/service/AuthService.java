package com.departement.fichedevoeux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.departement.fichedevoeux.model.Departement;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.DepartementRepository;
import com.departement.fichedevoeux.repository.ProfesseurRepository;

import DTO.LoginRequestDTO;
import DTO.RegisterRequestDTO;

@Service
public class AuthService {

    @Autowired
    private ProfesseurRepository professeurRepository;
    private DepartementRepository departementRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Inscription 
    public boolean register(RegisterRequestDTO dto) {
        if (dto.getEmail() == null || dto.getPassword() == null) return false;

        // il verifie si le compte existe déjà
        if (professeurRepository.findByEmail(dto.getEmail()) != null) {
            return false; // déjà inscrit
        }
        
		Departement dept = departementRepository.findByNomDepartement(dto.getDepartement());
		if(dept == null) {
			return false; // departement n'existe pas
		}
        
        Professeur prof = new Professeur();
        prof.setEmail(dto.getEmail());
        prof.setMotDePasse(passwordEncoder.encode(dto.getPassword()));
        prof.setNom(dto.getNom());
        prof.setDepartement(dept);
        prof.setChef(false); // par défaut pas chef

        professeurRepository.save(prof);
        return true;
    }

    // Connexion
    public boolean login(LoginRequestDTO dto) {
        Professeur prof = professeurRepository.findByEmail(dto.getEmail());
        if (prof == null) return false;

        return passwordEncoder.matches(dto.getPassword(), prof.getMotDePasse());
    }
}