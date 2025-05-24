package com.departement.fichedevoeux.service;

import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import DTO.ProfesseurDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfesseurService {
	
	

    @Autowired
    private ProfesseurRepository professeurRepository;
    
    @Autowired
    private ConversationService conversationService;

    // Récupérer tous les profs (en DTO si besoin)
    public List<ProfesseurDTO> getAll() {
        return professeurRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Trouver un prof par ID
    public ProfesseurDTO getById(Long id) {
        Optional<Professeur> prof = professeurRepository.findById(id);
        return prof.map(this::toDTO).orElse(null);
    }

    // Trouver par email
    public ProfesseurDTO getByEmail(String email) {
        Professeur prof = professeurRepository.findByEmail(email);
        return (prof != null) ? toDTO(prof) : null;
    }

    // Trouver tous les profs d’un département
    public List<ProfesseurDTO> getByDepartement(Long departementId, Long chefId) {
        return professeurRepository.findByDepartementId(departementId)
                .stream()
                .filter(p -> !p.isChef()) // on ignore les autres chefs
                .map(prof -> {
                    ProfesseurDTO dto = toDTO(prof);
                    Integer convId = conversationService.getExistingConversationId(prof.getId(), chefId);
                    dto.setConversationId(convId);
                    System.out.println("Professeur: " + prof.getNom() + ", Conversation ID: " + convId);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Récupérer tous les chefs
    public List<ProfesseurDTO> getChefs() {
        return professeurRepository.findByIsChefTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Ajouter un prof avec vérification de doublon
    public boolean createProfesseur(Professeur prof) {
        if (prof == null || professeurRepository.findByEmail(prof.getEmail()) != null) {
            return false;
        }
        professeurRepository.save(prof);
        return true;
    }

    // Conversion vers DTO
    private ProfesseurDTO toDTO(Professeur prof) {
        ProfesseurDTO dto = new ProfesseurDTO();

        dto.setId(prof.getId());
        dto.setNom(prof.getNom());
        dto.setEmail(prof.getEmail());
        dto.setDepartement(prof.getDepartement().getId());

        return dto;
    }
    
    public boolean updateEmailPref(Long profId, String emailPref) {
    	Professeur prof = professeurRepository.findById(profId).orElse(null);
    	if(prof == null) return false;
    	prof.setEmailPref(emailPref);
    	professeurRepository.save(prof);
    	return true;
    }
    
    public List<ProfesseurDTO> getProfesseursByDepartementForChef(Long departementId, String emailChef) {
        Professeur chef = professeurRepository.findByEmail(emailChef);
        if (chef == null || !chef.isChef()) {
            throw new RuntimeException("Accès interdit : seul un chef peut faire ça.");
        }

        return professeurRepository.findByDepartementId(departementId)
                .stream()
                .filter(p -> !p.isChef()) // On ignore les autres chefs
                .map(prof -> {
                    ProfesseurDTO dto = toDTO(prof);
                    Integer convId = conversationService.getOrCreateConversationId(prof.getId(), chef.getId());
                    dto.setConversationId(convId);
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    public Integer getConversationWithChef(Long profId) {
        Professeur prof = professeurRepository.findById(profId)
            .orElseThrow(() -> new RuntimeException("Professeur introuvable"));

        Professeur chef = professeurRepository.findChefByDepartement(prof.getDepartement().getId());
        if (chef == null) {
            throw new RuntimeException("Chef de département introuvable");
        }

        return conversationService.getExistingConversationId(prof.getId(), chef.getId());
    }

}

