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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    private void enregistrerChoix(List<ChoixDTO> liste, int semestre, Professeur prof) {
        int num = 1;
        for (ChoixDTO choix : liste) {
            Module module = moduleRepository.findByNom(choix.getModule());
            if (module != null) {
                Voeux voeux = new Voeux();
                voeux.setProfesseur(prof);
                voeux.setModule(module);
                voeux.setSemestre(semestre);
                voeux.setNature(choix.getNature());
                voeux.setNumChoix(num++);
                voeuxRepository.save(voeux);
            }
        }
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
}
