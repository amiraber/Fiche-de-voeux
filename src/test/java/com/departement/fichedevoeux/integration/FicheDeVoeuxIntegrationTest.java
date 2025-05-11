package com.departement.fichedevoeux.integration;

import com.departement.fichedevoeux.FichedevoeuxApplication;
import com.departement.fichedevoeux.model.BesoinsProf;
import com.departement.fichedevoeux.model.Departement;
import com.departement.fichedevoeux.model.Grade;
import com.departement.fichedevoeux.model.Module;
import com.departement.fichedevoeux.model.Parametre;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.model.Voeux;
import com.departement.fichedevoeux.repository.BesoinsProfRepository;
import com.departement.fichedevoeux.repository.DepartementRepository;
import com.departement.fichedevoeux.repository.ModuleRepository;
import com.departement.fichedevoeux.repository.ParametreRepository;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import com.departement.fichedevoeux.repository.VoeuxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import DTO.ChoixDTO;
import DTO.FormulaireRequestDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FichedevoeuxApplication.class)
@AutoConfigureMockMvc
//@ActiveProfiles("test") 
@AutoConfigureTestDatabase
@Transactional
//@WithMockUser(username = "Nom", roles = {"PROF"})
//@WithMockUser(username = "testuser", roles = {"PROF"})
public class FicheDeVoeuxIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ProfesseurRepository professeurRepository;
    @Autowired private DepartementRepository departementRepository;
    @Autowired private ModuleRepository moduleRepository;
    @Autowired private VoeuxRepository voeuxRepository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired
    private BesoinsProfRepository besoinsProfRepository;
    
    @Autowired
    private ParametreRepository parametreRepository;

    private Professeur savedProf;
    private Module moduleS1;
    private Module moduleS2;
    @BeforeEach
    public void setup() {
    	 // Clean up
        voeuxRepository.deleteAll();
        besoinsProfRepository.deleteAll();
        moduleRepository.deleteAll();
        professeurRepository.deleteAll();
        parametreRepository.deleteAll();

        Departement dept = new Departement("IA");
	    departementRepository.save(dept);
          
        // Save a Professeur
        Professeur prof = new Professeur();
        prof.setNom("Test");
        prof.setPrenom("Prof");
        prof.setEmail("test.prof@example.com");
        prof.setMotDePasse("cco");
        prof.setDepartement(dept);
        prof.setChef(false);
        savedProf = professeurRepository.save(prof);

        // Save Modules
        moduleS1 = new Module();
        moduleS1.setNom("Mathématiques I");
        moduleS1.setPallier("L1");
        moduleS1.setSpecialite("INFO");
        moduleS1.setSemestre("s1");
        moduleS1 = moduleRepository.save(moduleS1);

        moduleS2 = new Module();
        moduleS2.setNom("Algorithmique II");
        moduleS2.setPallier("L2");
        moduleS2.setSpecialite("INFO");
        moduleS2.setSemestre("s2");
        moduleS2 = moduleRepository.save(moduleS2);

        // Save a valid deadline
        Parametre param = new Parametre();
        param.setDeadline(LocalDate.now().plusDays(10));
        parametreRepository.save(param);
    }

    @Test
    void testSubmitFormulaire_success() throws Exception {
        // Create ChoixDTOs
        ChoixDTO choixS1 = new ChoixDTO("L1", "INFO", moduleS1.getNom(), "Cours");
        ChoixDTO choixS2 = new ChoixDTO("L2", "INFO", moduleS2.getNom(), "TD");

        // Create the form DTO
        FormulaireRequestDTO form = new FormulaireRequestDTO();
        form.setProfesseurId(savedProf.getId());
        form.setEmailPref("new.email@example.com");
        form.setNumBureau(102);
        form.setGrade(Grade.MAA);
        form.setWantsExtraCourses(true);
        form.setExtraHoursS1(4);
        form.setExtraHoursS2(2);
        form.setProposedLicences(1);
        form.setPrposedMaster(3);
        form.setSemestre1(List.of(choixS1));
        form.setSemestre2(List.of(choixS2));

        // Perform the request
        mockMvc.perform(post("/api/form/submit")
        		.with(user("testuser").roles("PROF")) // mock user with role
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Formulaire soumis avec succès.")));

        // Verify Professeur info updated
        Professeur updatedProf = professeurRepository.findById(savedProf.getId()).orElseThrow();
        assertEquals("new.email@example.com", updatedProf.getEmailPref());
        assertEquals(102, updatedProf.getNumBureau());
        assertEquals(Grade.MAA, updatedProf.getGrade());

        // Verify BesoinsProf
        List<BesoinsProf> besoinsList = besoinsProfRepository.findAll();
        assertEquals(1, besoinsList.size());
        BesoinsProf besoins = besoinsList.get(0);
        assertEquals(savedProf.getId(), besoins.getProfesseur().getId());
        assertEquals(4, besoins.getHeuresSuppS1());
        assertEquals(2, besoins.getHeuresSuppS2());
        assertEquals(1, besoins.getNbrPfeLicence());
        assertEquals(3, besoins.getNbrPfeMaster());
        assertEquals("SOUMIS", besoins.getStatut());

        // Verify Voeux
        List<Voeux> voeuxList = voeuxRepository.findAll();
        assertEquals(2, voeuxList.size());

        Voeux voeux1 = voeuxList.stream().filter(v -> v.getSemestre() == 1).findFirst().orElseThrow();
        assertEquals("Mathématiques I", voeux1.getModule().getNom());
        assertEquals("Cours", voeux1.getNature());

        Voeux voeux2 = voeuxList.stream().filter(v -> v.getSemestre() == 2).findFirst().orElseThrow();
        assertEquals("Algorithmique II", voeux2.getModule().getNom());
        assertEquals("TD", voeux2.getNature());
    }
    
    @Test
    public void testSubmitFormulaire_professeurNotFound() throws Exception {
        // Given: an ID that doesn't exist
        Long invalidProfId = 9999L;

        FormulaireRequestDTO form = new FormulaireRequestDTO();
        form.setProfesseurId(invalidProfId); // invalid
        form.setEmailPref("ghost@example.com");
        form.setGrade(Grade.MAB);
        form.setNumBureau(0);
        form.setWantsExtraCourses(false);
        form.setExtraHoursS1(0);
        form.setExtraHoursS2(0);
        form.setProposedLicences(0);
        form.setPrposedMaster(0);
        form.setSemestre1(Collections.emptyList());
        form.setSemestre2(Collections.emptyList());

        mockMvc.perform(post("/api/form/submit")
        		.with(user("testuser").roles("PROF")) // mock user with role
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Échec de la soumission du formulaire."));
    }

    
    @Test
    public void testCheckStatus_submitted() throws Exception {
    	
    	Departement depa = new Departement("CS");
    	departementRepository.save(depa);
    	
        Professeur prof = new Professeur();
        prof.setNom("Submit");
        prof.setPrenom("Check");
        prof.setDepartement(depa);
        prof.setEmail("submit@status.com");
        prof.setGrade(Grade.MCA);
        prof.setMotDePasse("check123");
        professeurRepository.save(prof);

        Module module = new Module();
        module.setNom("Algo");
        module.setPallier("whatever");
        module.setSemestre("s3");
        moduleRepository.save(module);

        Voeux voeux = new Voeux();
        voeux.setProfesseur(prof);
        voeux.setModule(module);
        voeux.setSemestre(1);
        voeux.setNature("Cours");
        voeux.setAnnee(LocalDate.now().getYear());
        voeux.setNumChoix(1);
        voeuxRepository.save(voeux);

        mockMvc.perform(get("/api/form/status")
        		.with(user("testuser").roles("CHEF_DEP"))
                .param("professeurId", String.valueOf(prof.getId())))
            .andExpect(status().isOk())
            .andExpect(content().string("Statut : Soumis"));
    }

    
    @Test
    public void testCheckStatus_notSubmitted() throws Exception {
    	
    	Departement depat = new Departement("CS");
    	departementRepository.save(depat);
    	
        Professeur prof = new Professeur();
        prof.setNom("Test");
        prof.setPrenom("User");
        prof.setEmail("test@no-submission.com");
        prof.setGrade(Grade.MCB);
        prof.setMotDePasse("test123");
        prof.setDepartement(depat);
        professeurRepository.save(prof);

        mockMvc.perform(get("/api/form/status")
        		.with(user("testuser").roles("CHEF_DEP"))
                .param("professeurId", String.valueOf(prof.getId())))
            .andExpect(status().isOk())
            .andExpect(content().string("Statut : Non soumis"));
    }

    @WithMockUser(username = "user", roles = {"PROF"})
    @Test
    public void testIsFormLocked_unlocked() throws Exception {
        // Setup: create a Parametre with a future deadline
        Parametre param = new Parametre();
        param.setDeadline(LocalDate.now().plusDays(5));
        parametreRepository.save(param);

        mockMvc.perform(get("/api/form/locked"))
        //.with(user("user").roles("PROF"))

            .andExpect(status().isOk())
            .andExpect(content().string("Formulaire actuellement déverrouillé"));
    }

    @WithMockUser(username = "user", roles = {"PROF"}) 
    @Test
    public void testIsFormLocked_locked() throws Exception {
        // Setup: create a Parametre with a past deadline
        Parametre param = new Parametre();
        param.setDeadline(LocalDate.now().minusDays(1));
        parametreRepository.save(param);

        mockMvc.perform(get("/api/form/locked"))
        //.with(user("user").roles("PROF"))

            .andExpect(status().isOk())
            .andExpect(content().string("Formulaire actuellement verrouillé"));
    }

    
    @Test
    @Transactional
    public void testGetFiche_returnsCurrentChoices() throws Exception {
        // Setup
    	Departement depo = new Departement("CS");
    	departementRepository.save(depo);
    	
        Professeur profe = new Professeur();
        profe.setNom("Test");
        profe.setPrenom("User");
        profe.setEmail("test@no-submission.com");
        profe.setGrade(Grade.MCB);
        profe.setMotDePasse("test123");
        profe.setDepartement(depo);
        professeurRepository.save(profe);
        
        int currentYear = LocalDate.now().getYear();

      Module  moduleS = new Module();
        moduleS.setNom("Java");
        moduleS.setPallier("L2");
        moduleS.setSpecialite("INFO");
        moduleS.setSemestre("s2");
        moduleS = moduleRepository.save(moduleS);// Add other required fields
        
        Voeux voeu = new Voeux();
        voeu.setProfesseur(profe);
        voeu.setModule(moduleS);
        voeu.setSemestre(1);
        voeu.setNature("Cours");
        voeu.setAnnee(currentYear);
        voeu.setNumChoix(1);
        voeuxRepository.save(voeu);

        mockMvc.perform(get("/api/form/get")
                .param("professeurId", String.valueOf(profe.getId()))
                .with(user("user").roles("PROF")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].module.nom").value("Java"))
            .andExpect(jsonPath("$[0].semestre").value(1));
    }

    
    @Test
    @Transactional
    public void testPreRemplirFormulaireAnneePrecedente_returnsPreviousYearChoices() throws Exception {
    	 // Setup
    	Departement depo = new Departement("CS");
    	departementRepository.save(depo);
    	
        Professeur profe = new Professeur();
        profe.setNom("Test");
        profe.setPrenom("User");
        profe.setEmail("test@no-submission.com");
        profe.setGrade(Grade.MCB);
        profe.setMotDePasse("test123");
        profe.setDepartement(depo);
        professeurRepository.save(profe);
        
        int lastYear = LocalDate.now().getYear() - 1;

        Module  moduleS = new Module();
        moduleS.setNom("Python");
        moduleS.setPallier("L2");
        moduleS.setSpecialite("INFO");
        moduleS.setSemestre("s2");
        moduleS = moduleRepository.save(moduleS);// Add other required fields
        // Add required fields
        
        Voeux voeu = new Voeux();
        voeu.setProfesseur(profe);
        voeu.setModule(moduleS);
        voeu.setSemestre(2);
        voeu.setNature("TP");
        voeu.setAnnee(lastYear);
        voeu.setNumChoix(1);
        voeuxRepository.save(voeu);

        mockMvc.perform(put("/api/form/formulaire-choix")
                .param("professeurId", String.valueOf(profe.getId()))
                .param("copierAnneePrecedente", "true")
                .with(user("user").roles("PROF")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].module.nom").value("Python"))
            .andExpect(jsonPath("$[0].semestre").value(2));
    }

    @Test
    @Transactional
    public void testGetModulesFiltres_returnsCorrectModules() throws Exception {
        // Save a matching module
        Module module = new Module("L3", "Informatique", "1", "Java");
        moduleRepository.save(module);

        // Save a non-matching module
        Module other = new Module("M1", "Maths", "2", "Python");
        moduleRepository.save(other);

        // Perform GET request with filters
        mockMvc.perform(get("/api/form/modules")
                .param("semestre", "1")
                .param("pallier", "L3")
                .param("specialite", "Informatique")
                .with(user("user").roles("PROF")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].nom").value("Java"))
            .andExpect(jsonPath("$[0].semestre").value("1"))
            .andExpect(jsonPath("$[0].pallier").value("L3"))
            .andExpect(jsonPath("$[0].specialite").value("Informatique"));
    }

    @Test
    @Transactional
    public void testGetSpecialitesFiltres_returnsCorrectSpecialites() throws Exception {
        // Two modules with different specialites but same semestre and pallier
        moduleRepository.save(new Module("L3", "Informatique", "1", "Java"));
        moduleRepository.save(new Module("L3", "Maths", "1", "Python"));

        // Add an irrelevant module
        moduleRepository.save(new Module("M2", "Physique", "2", "C++"));

        // Perform request
        mockMvc.perform(get("/api/form/specialites")
                .param("semestre", "1")
                .param("pallier", "L3")
                .with(user("user").roles("PROF")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[?(@ == 'Informatique')]").exists())
            .andExpect(jsonPath("$[?(@ == 'Maths')]").exists());
    }

}