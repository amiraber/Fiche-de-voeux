package com.departement.fichedevoeux.service;

import com.departement.fichedevoeux.model.Module;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.model.Voeux;
import com.departement.fichedevoeux.repository.ModuleRepository;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import com.departement.fichedevoeux.repository.VoeuxRepository;
import com.departement.fichedevoeux.repository.ParametreRepository;
import com.departement.fichedevoeux.model.Parametre;

import DTO.FormulaireRequestDTO;
import DTO.ChoixDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FicheDeVoeuxService {

    @Autowired
    private ProfesseurRepository professeurRepository;

    
    @Autowired
    private VoeuxRepository voeuxRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ParametreRepository parametreRepository;

    // 1. Soumettre le formulaire
    public boolean submitFormulaire(FormulaireRequestDTO form) {
    	if(isFormLocked()) {
    		return false;
    	}
    	
        Professeur prof = professeurRepository.findById(form.getProfesseurId()).orElse(null);
        if (prof == null) return false;

        // Supprimer les anciens vœux du prof
        List<Voeux> anciensVoeux = voeuxRepository.findByProfesseurId(prof.getId());
        voeuxRepository.deleteAll(anciensVoeux);

        // Enregistrer les nouveaux choix semestre 1
        enregistrerChoix(form.getSemestre1(), 1, prof);

        // Semestre 2
        enregistrerChoix(form.getSemestre2(), 2, prof);

        return true;
    }
    
    
    
    private boolean enregistrerChoix(List<ChoixDTO> liste, int semestre, Professeur prof) {
        // Étape 1: Récupérer tous les noms des modules de la liste de choix
        List<String> nomsModules = liste.stream()
                                        .map(ChoixDTO::getModule)  // Récupère le nom de chaque module
                                        .collect(Collectors.toList());  // Crée une liste avec tous les noms des modules

        // Étape 2: Récupérer tous les modules dans la base de données d'un coup
        List<Module> modules = moduleRepository.findByNomIn(nomsModules);  // Recherche tous les modules dont les noms sont dans la liste

        // Étape 3: Créer un dictionnaire (Map) pour accéder rapidement aux modules par nom
        Map<String, Module> moduleMap = modules.stream()
                                               .collect(Collectors.toMap(Module::getNom, module -> module));  // Crée un Map { nom -> module }

        // Étape 4: Parcourir la liste des choix et enregistrer les voeux
        int num = 1;
        for (ChoixDTO choix : liste) {
            Module module = moduleMap.get(choix.getModule());  // Cherche le module par son nom dans le Map

            // Si le module est trouvé, on l'enregistre
            if (module != null) {
                Voeux voeux = new Voeux();
                voeux.setProfesseur(prof);
                voeux.setModule(module);
                voeux.setSemestre(semestre);
                voeux.setNature(choix.getNature());
                voeux.setNumChoix(num++);
                voeuxRepository.save(voeux);  // Sauvegarde l'objet Voeux dans la base
            } else {
               return false;
            }
        }
        return true;
    }
    
    
    
    
    // 2. Vérifie si un prof a déjà soumis
    public boolean hasSubmitted(Long profId) {
        return !voeuxRepository.findByProfesseurId(profId).isEmpty();
    }

    // 3. Vérifie si la fiche est verrouillée (deadline dépassée)
    public boolean isFormLocked() {
        Parametre deadlineParam = parametreRepository.findTopByOrderByIdDesc();
        if (deadlineParam == null) return false;
        return LocalDate.now().isAfter(deadlineParam.getDeadline());
    }
    
    // pour la modification sans ressaisir tout
    public List<Voeux> getVoeuxDuProf(Long profId){
    	return voeuxRepository.findByProfesseurId(profId);
    }
    
    public List<Voeux> getChoixAnneePrecedente(Long professeurId) {
        LocalDate currentDate = LocalDate.now();
        int anneePrecedente = currentDate.getYear() - 1; // L'année précédente
        return voeuxRepository.findByProfesseurIdAndAnnee(professeurId, anneePrecedente);
    }
    
    
    // Filtrer les modules par semestre, pallier et spécialité
    public List<Module> filterModules(int semestre, String pallier, String specialite) {
        return moduleRepository.findBySemestreAndPallierAndSpecialite(semestre, pallier, specialite);
    }

    // Filtrer les spécialités par semestre et pallier 
    public List<String> filterSpecialites(int semestre, String pallier) {
        return moduleRepository.findDistinctSpecialitesBySemestreAndPallier(semestre, pallier);
    }
}
