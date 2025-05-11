package com.departement.fichedevoeux.integration;

import com.departement.fichedevoeux.FichedevoeuxApplication;
import com.departement.fichedevoeux.model.Departement;
import com.departement.fichedevoeux.model.Grade;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.DepartementRepository;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FichedevoeuxApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ProfesseurControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private DepartementRepository departementRepository;

    private Professeur prof;
    private Departement departement;

    @BeforeEach
    public void setup() {
        departement = new Departement();
        departement.setNomDepartement("Informatique");
        departementRepository.save(departement);

        prof = new Professeur();
        prof.setNom("Smith");
        prof.setPrenom("John");
        prof.setEmail("jsmith@example.com");
        prof.setMotDePasse("password");
        prof.setDepartement(departement);
        prof.setChef(true);
        prof.setGrade(Grade.MCA);
        prof.setNumBureau(101);
        prof.setEmailPref("john.pref@example.com");
        professeurRepository.save(prof);
    }

    @Test
    @WithMockUser(roles = "PROF")
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/api/professeurs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
            .andExpect(jsonPath("$[0].email", is(prof.getEmail())));
    }

    @Test
    @WithMockUser(roles = "PROF")
    public void testGetById() throws Exception {
        mockMvc.perform(get("/api/professeurs/" + prof.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email", is(prof.getEmail())));
    }

    @Test
    @WithMockUser(roles = "PROF")
    public void testGetByEmail() throws Exception {
        mockMvc.perform(get("/api/professeurs/by-email")
                .param("email", prof.getEmail()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nom", is(prof.getNom())));
    }

    @Test
    @WithMockUser(roles = "PROF")
    public void testGetByDepartment() throws Exception {
        mockMvc.perform(get("/api/professeurs/by-departement/" + departement.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].departement", is(departement.getNomDepartement())));
    }

    @Test
    @WithMockUser(roles = "PROF")
    public void testGetChefs() throws Exception {
        mockMvc.perform(get("/api/professeurs/chef")
        		 )
           
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].email", is(prof.getEmail())));
    }

    @Test
    @WithMockUser(roles = "PROF")
    public void testCreateProfesseur() throws Exception {
        String body = """
            {
                "nom": "Doe",
                "prenom": "Jane",
                "email": "jane.doe@example.com",
                "motDePasse": "secret",
                "grade": "MCA",
                "departement": { "id": %d },
                "isChef": false,
                "numBureau": 102
            }
        """.formatted(departement.getId());

        mockMvc.perform(post("/api/professeurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("succès")));
    }

    @Test
    @WithMockUser(roles = "PROF")
    public void testUpdateEmailPref_success() throws Exception {
        mockMvc.perform(put("/api/professeurs/emai-Pref")
                .param("profId", String.valueOf(prof.getId()))
                .param("emailPref", "updated@example.com"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("mis à jour")));
    }

    @Test
    @WithMockUser(roles = "PROF")
    public void testUpdateEmailPref_fail() throws Exception {
        mockMvc.perform(put("/api/professeurs/emai-Pref")
                .param("profId", "99999")
                .param("emailPref", "unknown@example.com"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("introuvable")));
    }

    @Test
    @WithMockUser(roles = "PROF")
    public void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/api/professeurs/test"))
            .andExpect(status().isOk())
            .andExpect(content().string("Professeur Controller is working!"));
    }
} 
