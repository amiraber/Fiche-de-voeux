package com.departement.fichedevoeux.service;

import com.departement.fichedevoeux.controller.FormulaireVerrouilleException;
import com.departement.fichedevoeux.model.*;
import com.departement.fichedevoeux.model.Module;
import com.departement.fichedevoeux.repository.*;

import DTO.ChoixDTO;
import DTO.FormulaireCompletDTO;
import DTO.FormulaireRequestDTO;
import DTO.ModuleDTO;
import DTO.ProfesseurDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
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
    
    @Autowired
    private FicheDeVoeuxRepository ficheDeVoeuxRepository;

    // 1. Soumettre un formulaire complet
    @Transactional
    public boolean submitFormulaire(FormulaireRequestDTO form) {
    	
    	log.info(">>> /SUBMIT SERVICE called with {}", form);
    	
    	Professeur prof = professeurRepository.findById(form.getProfesseurId()).orElse(null);
        if (prof == null) return false;

    	
        int currentYear = LocalDate.now().getYear();
        List<Voeux> anciens = voeuxRepository.findByProfesseurIdAndAnnee(prof.getId(), currentYear);

        // Fusionne les deux semestres
        List<ChoixDTO> nouveauxChoix = new ArrayList<>();
        nouveauxChoix.addAll(form.getSemestre1());
        nouveauxChoix.addAll(form.getSemestre2());

        // Supprime uniquement les anciens voeux qui ne sont plus présents à l’identique
        for (Voeux ancien : anciens) {
            boolean existeEncore = nouveauxChoix.stream().anyMatch(nouveau -> {
                ModuleDTO nouveauMod = nouveau.getModule();
                return
                    ancien.getModule().getNom().equals(nouveauMod.getNom()) &&
                    ancien.getModule().getPallier().equals(nouveauMod.getNiveau()) &&
                    ancien.getModule().getSpecialite().equals(nouveauMod.getSpecialite()) &&
                    nouveau.getSemestre().equalsIgnoreCase(ancien.getSemestre()) &&
                    nouveau.getNumChoix() == ancien.getNumChoix() &&
                    nouveau.getNature().contains(ancien.getNature()); // ancien.getNature() est un seul String (ex: "TD")
            });

            if (!existeEncore) {
                voeuxRepository.delete(ancien);
                log.info("🗑️ Supprimé voeu obsolète : {}", ancien);
            }
        }

    	
        if (isFormLocked()) {
            throw new FormulaireVerrouilleException("Formulaire verrouillé : deadline dépassée.");
        }


        
        // Mettre à jour email préféré, grade, bureau
        prof.setEmailPref(form.getEmailPref());
        prof.setNumBureau(form.getNumBureau());
        prof.setGrade(form.getGrade());
        professeurRepository.save(prof);
        
        //hhh
       


        // Créer BesoinsProf
        BesoinsProf besoins = new BesoinsProf();
        besoins.setProfesseur(prof);
        besoins.setAnneeScolaire(getCurrentAcademicYear());
        besoins.setHeuresSuppS1(form.isWantsExtraCourses() ? form.getExtraHoursS1() : 0);
        besoins.setHeuresSuppS2(form.isWantsExtraCourses() ? form.getExtraHoursS2() : 0);
        besoins.setNbrPfeLicence(form.getProposedLicences());
        besoins.setNbrPfeMaster(form.getProposedMaster());
        besoins.setStatut("SOUMIS");
        besoinsProfRepository.save(besoins);

        

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
        
        System.out.println("📌 Grade reçu : " + form.getGrade());
        System.out.println("📌 Professeur ID : " + form.getProfesseurId());
        System.out.println("📌 Semestre 1 : " + form.getSemestre1());
        System.out.println("📌 Semestre 2 : " + form.getSemestre2());


        return okS1 && okS2;
             
    }

    private boolean enregistrerChoix(List<ChoixDTO> liste, int semestreInt, Professeur prof, int annee) {
        if (liste == null) return true;

        for (ChoixDTO c : liste) {
            Optional<Module> optionalModule = moduleRepository.findByNomAndPallierAndSpecialiteAndSemestre(
                c.getModule().getNom(),
                c.getModule().getNiveau(),
                c.getModule().getSpecialite(),
                "S" + semestreInt
            );

            if (optionalModule.isEmpty()) {
                log.warn("Module non trouvé en base pour : nom={}, niveau={}, specialite={}, semestre={}",
                    c.getModule().getNom(),
                    c.getModule().getNiveau(),
                    c.getModule().getSpecialite(),
                    "S" + semestreInt
                );
                continue;
            }

            Module module = optionalModule.get();

            for (String nature : c.getNature()) {
                boolean dejaExistant = voeuxRepository.existsByProfesseurAndModuleAndNatureAndSemestreAndNumChoix(
                    prof, module, nature, "S" + semestreInt, c.getNumChoix()
                );

                if (dejaExistant) {
                    log.warn("⚠️ Voeu déjà existant ignoré : module={}, nature={}, semestre={}, choix={}",
                        module.getNom(), nature, "S" + semestreInt, c.getNumChoix());
                    continue;
                }

                Voeux voeu = new Voeux();
                voeu.setAnnee(annee);
                voeu.setModule(module);
                voeu.setNature(nature);
                voeu.setNumChoix(c.getNumChoix());
                voeu.setProfesseur(prof);
                voeu.setSemestre("S" + semestreInt);

                voeuxRepository.save(voeu);
                log.info("✅ Voeu enregistré : module={}, nature={}, semestre={}, choix={}",
                    module.getNom(), nature, "S" + semestreInt, c.getNumChoix());
            }
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
    
    public List<ChoixDTO> getVoeuxAsDTO(Long profId) {
        List<Voeux> voeux = voeuxRepository.findByProfesseurId(profId);
        return voeux.stream().map(v -> {
            ChoixDTO dto = new ChoixDTO();

            ModuleDTO moduleDTO = new ModuleDTO();
            moduleDTO.setNom(v.getModule().getNom());
            moduleDTO.setNiveau(v.getModule().getPallier());
            moduleDTO.setSpecialite(v.getModule().getSpecialite());

            dto.setModule(moduleDTO);
            dto.setNature(List.of(v.getNature()));


            dto.setNature(List.of(v.getNature().split(","))); // si nature est une String concaténée

            return dto;
        }).collect(Collectors.toList());
    }

    public FormulaireCompletDTO getFormulaireDuProf(Long professeurId) {
        List<Voeux> voeux = voeuxRepository.findByProfesseurIdAndAnnee(professeurId, Year.now().getValue());
        List<BesoinsProf> besoinsList = besoinsProfRepository.findByProfesseurId(professeurId);
        BesoinsProf besoins = besoinsList.isEmpty() ? null : besoinsList.get(0);
        Professeur prof = professeurRepository.findById(professeurId).orElseThrow();

        FormulaireCompletDTO dto = new FormulaireCompletDTO();

        List<ChoixDTO> s1 = new ArrayList<>();
        List<ChoixDTO> s2 = new ArrayList<>();

        for (Voeux v : voeux) {
            ChoixDTO c = convertToDTO(v);
            if ("S1".equalsIgnoreCase(v.getSemestre())) {
                s1.add(c);
            } else {
                s2.add(c);
            }
        }

        dto.setSemestre1(s1);
        dto.setSemestre2(s2);

        if (besoins != null) {
        	dto.setExtraHoursS1(besoins.getNbrHeuresSuppS1() != null ? besoins.getNbrHeuresSuppS1() : 0);
        	dto.setExtraHoursS2(besoins.getNbrHeuresSuppS2() != null ? besoins.getNbrHeuresSuppS2() : 0);
        	dto.setWantsExtraCourses(
        		    (besoins.getNbrHeuresSuppS1() != null && besoins.getNbrHeuresSuppS1() > 0) ||
        		    (besoins.getNbrHeuresSuppS2() != null && besoins.getNbrHeuresSuppS2() > 0)
        		);
            dto.setProposedLicence(besoins.getNbrPfeLicence() != null ? besoins.getNbrPfeLicence() : 0);
            dto.setProposedMaster(besoins.getNbrPfeMaster() != null ? besoins.getNbrPfeMaster() : 0);
        }

        dto.setGrade(prof.getGrade() != null ? prof.getGrade().name() : "");
        dto.setNumBureau(prof.getNumBureau() != null ? String.valueOf(prof.getNumBureau()) : "");
        dto.setEmailPref(prof.getEmailPref());

        return dto;
    }

    private ChoixDTO convertToDTO(Voeux voeux) {
        ChoixDTO dto = new ChoixDTO();
        Module module = voeux.getModule();

        ModuleDTO moduleDTO = new ModuleDTO();
        moduleDTO.setNom(module.getNom());
        moduleDTO.setPallier(module.getPallier());
        moduleDTO.setSpecialite(module.getSpecialite());
        moduleDTO.setSemestre(module.getSemestre());

        dto.setModule(moduleDTO);
        dto.setNature(Arrays.asList(voeux.getNature().split(",")));
        dto.setSemestre(voeux.getSemestre());
        dto.setNumChoix(voeux.getNumChoix());

        return dto;
    }
    
    public List<Map<String, Object>> getProfesseursInscritsParDepartement(Long departementId) {
        List<Long> profsAvecFiche = ficheDeVoeuxRepository.findDistinctProfesseurIds();
        if (profsAvecFiche.isEmpty()) return new ArrayList<>();

        List<Professeur> professeurs = professeurRepository.findByIdInAndDepartementId(profsAvecFiche, departementId);

        return professeurs.stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("name", p.getNom());
            map.put("email", p.getEmail());
            map.put("grade", p.getGrade());
            map.put("departement", p.getDepartement().getNomDepartement());
            return map;
        }).collect(Collectors.toList());
    }
    
}
