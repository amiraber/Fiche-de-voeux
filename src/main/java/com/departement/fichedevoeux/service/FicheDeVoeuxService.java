package com.departement.fichedevoeux.service;

import com.departement.fichedevoeux.model.*;
import com.departement.fichedevoeux.model.Module;
import com.departement.fichedevoeux.repository.*;

import DTO.ChoixDTO;
import DTO.FormulaireRequestDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;



@Service
public class FicheDeVoeuxService {
	
	private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private VoeuxRepository voeuxRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ParametreRepository parametreRepository;

    @Autowired
    private BesoinsProfRepository besoinsProfRepository;

    // 1. Soumettre un formulaire complet
    @Transactional
    public boolean submitFormulaire(FormulaireRequestDTO form) {
    	
    	log.info(">>> /SUBMIT SERVICE called with {}", form);
    	
        if (isFormLocked()) return false;

        Professeur prof = professeurRepository.findById(form.getProfesseurId()).orElse(null);
        if (prof == null) return false;

        // Mettre à jour email préféré, grade, bureau
        prof.setEmailPref(form.getEmailPref());
        prof.setNumBureau(form.getNumBureau());
        prof.setGrade(form.getGrade());
        professeurRepository.save(prof);

        // Créer BesoinsProf
        BesoinsProf besoins = new BesoinsProf();
        besoins.setProfesseur(prof);
        besoins.setAnneeScolaire(getCurrentAcademicYear());
        besoins.setHeuresSuppS1(form.isWantsExtraCourses() ? form.getExtraHoursS1() : 0);
        besoins.setHeuresSuppS2(form.isWantsExtraCourses() ? form.getExtraHoursS2() : 0);
        besoins.setNbrPfeLicence(form.getProposedLicences());
        besoins.setNbrPfeMaster(form.getPrposedMaster());
        besoins.setStatut("SOUMIS");
        besoinsProfRepository.save(besoins);

        // Supprimer les anciens voeux
        int currentYear = LocalDate.now().getYear();
        List<Voeux> anciens = voeuxRepository.findByProfesseurIdAndAnnee(prof.getId(), currentYear);
        voeuxRepository.deleteAll(anciens);

        // Enregistrer les nouveaux choix
        boolean okS1 = enregistrerChoix(form.getSemestre1(), 1, prof, currentYear);
        boolean okS2 = enregistrerChoix(form.getSemestre2(), 2, prof, currentYear);
        
        /* ===== Trace recap ===== */
        String recapS1 = form.getSemestre1().stream()
                .map(c -> c.getModule() + "-" + c.getNature())
                .collect(java.util.stream.Collectors.joining(", "));

        String recapS2 = form.getSemestre2().stream()
                .map(c -> c.getModule() + "-" + c.getNature())
                .collect(java.util.stream.Collectors.joining(", "));


        log.info("✓ SUBMIT completed for Prof {} ({}) | S1:[{}] | S2:[{}] | Ext hrs S1={} S2={} | PFE L={} PFE M={}",
                prof.getId(), prof.getNom() + " " + prof.getPrenom(),
                recapS1, recapS2,
                besoins.getHeuresSuppS1(), besoins.getHeuresSuppS2(),
                besoins.getNbrPfeLicence(), besoins.getNbrPfeMaster());
        /* ======================= */

        return okS1 && okS2;
             
    }

    private boolean enregistrerChoix(List<ChoixDTO> liste, int semestre, Professeur prof, int annee) {
       // List<String> nomsModules = liste.stream().map(ChoixDTO::getModule).collect(Collectors.toList());
        List<String> nomsModules = liste.stream().map(ChoixDTO::getModule).toList();
        
        List<Module> modules = moduleRepository.findByNomIn(nomsModules);
        
       // Map<String, Module> moduleMap = modules.stream().collect(Collectors.toMap(Module::getNom, m -> m));
     // 2. Allow duplicates by grouping under the same name
        Map<String, List<Module>> moduleMap = modules.stream()
                .collect(Collectors.groupingBy(Module::getNom));
        
        
        int numChoix = 1;
        for (ChoixDTO choix : liste) {
            //Module module = moduleMap.get(choix.getModule());
        	List<Module> candidates = moduleMap.get(choix.getModule());
        	
           // if (module == null) return false;
        	 if (candidates == null || candidates.isEmpty()) return false;
        	 
        	  // If more than one candidate remains, choose more precisely here
             Module module = candidates.get(0);
             
            Voeux voeux = new Voeux(prof, module, semestre, choix.getNature(), annee, numChoix++);
            voeuxRepository.save(voeux);
        }
        return true;
    }

    // 2. Vérifier si déjà soumis
    public boolean hasSubmitted(Long profId) {
    	
    	log.info(">>> /State SERVICE called with {}", profId);
    	
        return !voeuxRepository.findByProfesseurId(profId).isEmpty();
    }

    // 3. Vérifier si formulaire verrouillé
    public boolean isFormLocked() {
        Parametre deadline = parametreRepository.findTopByOrderByIdDesc();
        return deadline != null && LocalDate.now().isAfter(deadline.getDeadline());
    }

    // 4. Obtenir les voeux d’un prof (pour modification)
    public List<Voeux> getVoeuxDuProf(Long profId) {
        return voeuxRepository.findByProfesseurId(profId);
    }

    // 5. Récupérer les voeux de l’année précédente
    public List<Voeux> getChoixAnneePrecedente(Long profId) {
        int anneePrecedente = LocalDate.now().getYear() - 1;
        return voeuxRepository.findByProfesseurIdAndAnnee(profId, anneePrecedente);
    }

    // 6. Pour les listes déroulantes
    public List<Module> filterModules(String semestre, String pallier, String specialite) {
    	
    	log.info(">>> /modules SERVICE called ");
    	
        return moduleRepository.findBySemestreAndPallierAndSpecialite(semestre, pallier, specialite);
    }

    public List<String> filterSpecialites(String semestre, String pallier) {
    	
    	log.info(">>> /specialites SERVICE called ");
    	
        return moduleRepository.findDistinctSpecialitesBySemestreAndPallier(semestre, pallier);
    }

    // Utilitaire
    private String getCurrentAcademicYear() {
        int year = LocalDate.now().getYear();
        return year + "-" + (year + 1);
    }
    
    
}
