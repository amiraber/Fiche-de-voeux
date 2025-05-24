package com.departement.fichedevoeux.controller;

import com.departement.fichedevoeux.service.ProfesseurService;
import DTO.ProfesseurDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(controllers = ProfesseurController.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // <--- disables Spring Security filters
//@WebMvcTest(ProfesseurController.class)
public class ProfesseurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfesseurService professeurService; // Mocks the service layer for testing

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnListOfProfesseurs() throws Exception {
        ProfesseurDTO prof1 = new ProfesseurDTO();
        prof1.setId(1L);
        prof1.setNom("Doe");
        prof1.setEmail("doe@example.com");

        List<ProfesseurDTO> profs = Arrays.asList(prof1);
        when(professeurService.getAll()).thenReturn(profs);

        mockMvc.perform(get("/api/professeurs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nom").value("Doe"));
    }

    @Test
    void shouldReturnProfesseurById() throws Exception {
        ProfesseurDTO prof = new ProfesseurDTO();
        prof.setId(1L);
        prof.setNom("Smith");

        when(professeurService.getById(1L)).thenReturn(prof);

        mockMvc.perform(get("/api/professeurs/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nom").value("Smith"));
    }

    @Test
    void shouldReturnProfesseurByEmail() throws Exception {
        ProfesseurDTO prof = new ProfesseurDTO();
        prof.setId(2L);
        prof.setEmail("test@univ.com");

        when(professeurService.getByEmail("test@univ.com")).thenReturn(prof);

        mockMvc.perform(get("/api/professeurs/by-email")
                .param("email", "test@univ.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@univ.com"));
    }

    @Test
    void shouldReturnProfesseursByDepartement() throws Exception {
        ProfesseurDTO prof = new ProfesseurDTO();
        prof.setDepartement(1L);

       // when(professeurService.getByDepartement(5L)).thenReturn(List.of(prof));

        mockMvc.perform(get("/api/professeurs/by-departement/5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].departement").value("Informatique"));
    }

    @Test
    void shouldReturnListOfChefs() throws Exception {
        ProfesseurDTO prof = new ProfesseurDTO();
        prof.setNom("Chef");

        when(professeurService.getChefs()).thenReturn(List.of(prof));

        mockMvc.perform(get("/api/professeurs/chef"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nom").value("Chef"));
    }

    @Test
    void shouldCreateProfesseurSuccessfully() throws Exception {
        when(professeurService.createProfesseur(any())).thenReturn(true);

        String json = "{\"nom\":\"Doe\", \"email\":\"doe@univ.com\"}";

        mockMvc.perform(post("/api/professeurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(content().string("Professeur créé avec succès"));
    }

    @Test
    void shouldFailToCreateProfesseurWhenEmailExists() throws Exception {
        when(professeurService.createProfesseur(any())).thenReturn(false);

        String json = "{\"nom\":\"Doe\", \"email\":\"used@univ.com\"}";

        mockMvc.perform(post("/api/professeurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(content().string("Email déjà utilisé"));
    }
    @WithMockUser(username = "chef1", roles = "CHEF") // or the role you require
    @Test
    void shouldUpdateEmailPrefSuccessfully() throws Exception {
        when(professeurService.updateEmailPref(1L, "pref@univ.com")).thenReturn(true);

        mockMvc.perform(put("/api/professeurs/emai-Pref")
                .param("profId", "1")
                .param("emailPref", "pref@univ.com"))
            .andExpect(status().isOk())
            .andExpect(content().string("Email préféré mis à jour"));
    }

    @Test
    void shouldFailToUpdateEmailPrefIfProfNotFound() throws Exception {
        when(professeurService.updateEmailPref(99L, "notfound@univ.com")).thenReturn(false);

        mockMvc.perform(put("/api/professeurs/emai-Pref")
                .param("profId", "99")
                .param("emailPref", "notfound@univ.com"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Professeur introuvable"));
    }

    @Test
    void shouldReturnTestMessage() throws Exception {
        mockMvc.perform(get("/api/professeurs/test"))
            .andExpect(status().isOk())
            .andExpect(content().string("Professeur Controller is working!"));
    }
}
