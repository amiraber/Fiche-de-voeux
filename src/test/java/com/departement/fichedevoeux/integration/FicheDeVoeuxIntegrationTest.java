package com.departement.fichedevoeux.integration;

import com.departement.fichedevoeux.FichedevoeuxApplication;
import com.departement.fichedevoeux.model.Departement;
import com.departement.fichedevoeux.model.Module;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.DepartementRepository;
import com.departement.fichedevoeux.repository.ModuleRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FichedevoeuxApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional
@WithMockUser(username = "testuser", roles = {"PROF"})
public class FicheDeVoeuxIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ProfesseurRepository professeurRepository;
    @Autowired private DepartementRepository departementRepository;
    @Autowired private ModuleRepository moduleRepository;
    @Autowired private VoeuxRepository voeuxRepository;
    @Autowired private ObjectMapper objectMapper;

    private Professeur prof;
    private Module module;

    @BeforeEach
    public void setup() {
        voeuxRepository.deleteAll();
        moduleRepository.deleteAll();
        professeurRepository.deleteAll();
        departementRepository.deleteAll();

        Departement dep = new Departement("Informatique");
        departementRepository.save(dep);

        prof = new Professeur();
        prof.setNom("Amira");
        prof.setPrenom("Ali");
        prof.setEmail("amira@example.com");
        prof.setMotDePasse("pass");
        prof.setDepartement(dep);
        prof.setNumBureau(101);
        prof.setEmailPref("amira.pref@example.com");
        professeurRepository.save(prof);

        module = new Module("L3", "GL", "1", "Programmation Avancée");
        moduleRepository.save(module);
    }

    @Test
    public void testSubmitFormulaireIntegration() throws Exception {
        ChoixDTO choix1 = new ChoixDTO("L3", "GL", module.getNom(), "Cours");
        ChoixDTO choix2 = new ChoixDTO("L3", "GL", module.getNom(), "TP");

        FormulaireRequestDTO form = new FormulaireRequestDTO();
        form.setProfesseurId(prof.getId());
        form.setSemestre1(List.of(choix1));
        form.setSemestre2(List.of(choix2));
        form.setWantsExtraCourses(true);
        form.setExtraHoursS1(2);
        form.setExtraHoursS2(3);
        form.setProposedLicences(1);
        form.setPrposedMaster(1);

        mockMvc.perform(post("/api/form/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().string("Formulaire soumis avec succès."));

        assertThat(voeuxRepository.findByProfesseurId(prof.getId())).hasSize(2);
    }
}