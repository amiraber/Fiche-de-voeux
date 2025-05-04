package com.departement.fichedevoeux.controller;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.departement.fichedevoeux.config.SecurityConfig;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.service.ProfesseurService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import DTO.ProfesseurDTO;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProfesseurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfesseurService professeurService;

    @Test
    public void testGetAll_ShouldReturn200() throws Exception {
        when(professeurService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/professeurs"))
               .andExpect(status().isOk());
    }

    @Test
    public void testGetById_ShouldReturn200() throws Exception {
        Long id = 1L;
        when(professeurService.getById(id)).thenReturn(new ProfesseurDTO());

        mockMvc.perform(get("/api/professeurs/{id}", id))
               .andExpect(status().isOk());
    }

    @Test
    public void testGetByEmail_ShouldReturn200() throws Exception {
        String email = "test@example.com";
        when(professeurService.getByEmail(email)).thenReturn(new ProfesseurDTO());

        mockMvc.perform(get("/api/professeurs/by-email").param("email", email))
               .andExpect(status().isOk());
    }

    @Test
    public void testGetByDepartment_ShouldReturn200() throws Exception {
        Long depId = 2L;
        when(professeurService.getByDepartement(depId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/professeurs/by-departement/{id}", depId))
               .andExpect(status().isOk());
    }

    @Test
    public void testGetChefs_ShouldReturn200() throws Exception {
        when(professeurService.getChefs()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/professeurs/chef"))
               .andExpect(status().isOk());
    }

    @Test
    public void testCreate_ShouldReturnSuccessMessage() throws Exception {
        Professeur prof = new Professeur();
        when(professeurService.createProfesseur(any())).thenReturn(true);

        mockMvc.perform(post("/api/professeurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(prof)))
               .andExpect(status().isOk())
               .andExpect(content().string("Professeur créé avec succès"));
    }

    @Test
    public void testUpdateEmailPref_ShouldReturn200() throws Exception {
        when(professeurService.updateEmailPref(1L, "pref@mail.com")).thenReturn(true);

        mockMvc.perform(put("/api/professeurs/emai-Pref")
                .param("profId", "1")
                .param("emailPref", "pref@mail.com"))
               .andExpect(status().isOk())
               .andExpect(content().string("Email préféré mis à jour"));
    }

    @Test
    public void testHello_ShouldReturnWorkingMessage() throws Exception {
        mockMvc.perform(get("/api/professeurs/test"))
               .andExpect(status().isOk())
               .andExpect(content().string("Professeur Controller is working!"));
    }
}
