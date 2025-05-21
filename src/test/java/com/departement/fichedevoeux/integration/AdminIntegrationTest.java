package com.departement.fichedevoeux.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.departement.fichedevoeux.FichedevoeuxApplication;
import com.departement.fichedevoeux.model.Departement;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.model.Voeux;
import com.departement.fichedevoeux.model.Module; // ✅ Your JPA entity
import com.departement.fichedevoeux.model.Parametre;
import com.departement.fichedevoeux.repository.DepartementRepository;
import com.departement.fichedevoeux.repository.FicheDeVoeuxRepository;
import com.departement.fichedevoeux.repository.ModuleRepository;
import com.departement.fichedevoeux.repository.ParametreRepository;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import DTO.DeadlineRequestDTO;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FichedevoeuxApplication.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc // 💡 Required to inject MockMvc
@Transactional

@TestPropertySource(locations = "classpath:application.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class AdminIntegrationTest {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfesseurRepository professeurRepository;
    
    
    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private FicheDeVoeuxRepository ficheDeVoeuxRepository;

    @Autowired
    private ParametreRepository parametreRepository;
    
    @Autowired
    private ModuleRepository moduleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private final String USER_EMAIL = "alice@univ.tn";

    @BeforeAll
    void setUpUser() {
        Departement departement = new Departement("Info");
        departementRepository.save(departement);

        Professeur prof = new Professeur();
        prof.setChef(true);
        prof.setNom("Alice");
        prof.setPrenom("Doe");
        prof.setEmail(USER_EMAIL);
        prof.setMotDePasse(passwordEncoder.encode("pwd"));
        prof.setDepartement(departement);
        professeurRepository.save(prof);
        
      


       
    }

  
    @Test
    @WithUserDetails(value = "alice@univ.tn", userDetailsServiceBeanName = "customUserDetailsService")
    void exportExcel_ShouldReturnExcelFile_WhenUserIsChef() throws Exception {
      

        
        // Add and persist a Module
           Module module = new Module();
           module.setNom("Mathématiques");
           module.setPallier("L3");
           module.setSpecialite("Informatique");
           module.setSemestre("S1");
           module = moduleRepository.save(module); // Make sure moduleRepository is @Autowired
           
           Professeur prof = professeurRepository.findByEmail("alice@univ.tn");

           // Add a dummy Voeux to export
           Voeux voeux = new Voeux();
           voeux.setProfesseur(prof);
           voeux.setModule(module);
           voeux.setSemestre("1"); // changed to int since `semestre` is int in Voeux
           voeux.setNature("CM");
           voeux.setAnnee(2025);
           voeux.setNumChoix(1);
           ficheDeVoeuxRepository.save(voeux);
      

     // Perform the request
        mockMvc.perform(get("/api/admin/export"))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", "attachment; filename=voeux.xlsx\""))
            .andExpect(result -> assertThat(result.getResponse().getContentAsByteArray().length).isGreaterThan(0));
}
  
    
    @Test
    @WithUserDetails(value ="alice@univ.tn", userDetailsServiceBeanName = "customUserDetailsService")
    void getDeadlineStatus_ShouldReturnActive() throws Exception {
      
        
     // Set deadline first
        DeadlineRequestDTO request = new DeadlineRequestDTO();
        request.setDeadline(LocalDate.of(2025, 6, 30));
        mockMvc.perform(post("/api/admin/deadline")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

        // Then get it
        mockMvc.perform(get("/api/admin/deadline"))
            .andExpect(status().isOk())
            .andExpect(content().string("Deadline is active: 2025-06-30"));
    }

    
    @Test
    @WithUserDetails(value = "alice@univ.tn", userDetailsServiceBeanName = "customUserDetailsService")
    void setDeadline_ShouldUpdate_WhenUserIsChef() throws Exception {
        DeadlineRequestDTO request = new DeadlineRequestDTO();
        request.setDeadline(LocalDate.of(2025, 6, 30));

        mockMvc.perform(post("/api/admin/deadline")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().string("Deadline updated"));

        // Optional: assert DB state
        Parametre latest = parametreRepository.findTopByOrderByIdDesc();
        assertEquals(LocalDate.of(2025, 6, 30), latest.getDeadline());
    }

    
    
}