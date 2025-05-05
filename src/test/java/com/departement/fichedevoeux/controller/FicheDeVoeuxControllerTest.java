package com.departement.fichedevoeux.controller;

import com.departement.fichedevoeux.config.TestSecurityConfig;
import com.departement.fichedevoeux.model.Voeux;
import com.departement.fichedevoeux.service.FicheDeVoeuxService;
import DTO.FormulaireRequestDTO;
import DTO.ChoixDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(FicheDeVoeuxController.class)
public class FicheDeVoeuxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FicheDeVoeuxService ficheDeVoeuxService;

    @Autowired
    private ObjectMapper objectMapper;

    private FormulaireRequestDTO formulaire;

    @BeforeEach
    void setUp() {
        formulaire = new FormulaireRequestDTO();
        formulaire.setProfesseurId(1L);

        ChoixDTO choix1 = new ChoixDTO("pallier", "spécialité", "module", "nature");
        ChoixDTO choix2 = new ChoixDTO("pallier2", "spécialité2", "module2", "nature2");

        formulaire.setSemestre1(List.of(choix1));
        formulaire.setSemestre2(List.of(choix2));
    }

    @Test
    void testSubmitFormulaire_Valid() throws Exception {
        when(ficheDeVoeuxService.submitFormulaire(any(FormulaireRequestDTO.class))).thenReturn(true);

        mockMvc.perform(post("/api/form/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(formulaire)))
                .andExpect(status().isOk())
                .andExpect(content().string("Form submitted"));
    }

    @Test
    void testSubmitFormulaire_Invalid() throws Exception {
        when(ficheDeVoeuxService.submitFormulaire(any(FormulaireRequestDTO.class))).thenReturn(false);

        mockMvc.perform(post("/api/form/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(formulaire)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid form submission"));
    }

    @Test
    void testCheckStatus() throws Exception {
        when(ficheDeVoeuxService.hasSubmitted(1L)).thenReturn(true);

        mockMvc.perform(get("/api/form/status")
                .param("professorId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Status: Submitted"));
    }

    @Test
    void testIsFormLocked() throws Exception {
        when(ficheDeVoeuxService.isFormLocked()).thenReturn(false);

        mockMvc.perform(get("/api/form/locked"))
                .andExpect(status().isOk())
                .andExpect(content().string("Form is currently unlocked"));
    }

    @Test
    void testGetFiche() throws Exception {
        Voeux voeux = new Voeux(); // Tu peux set des attributs si besoin
        when(ficheDeVoeuxService.getVoeuxDuProf(1L)).thenReturn(List.of(voeux));

        mockMvc.perform(get("/api/form/get")
                .param("professeurId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testPreRemplirFormulaireAnneePrecedente_WhenChecked() throws Exception {
        Voeux voeux = new Voeux();
        when(ficheDeVoeuxService.getChoixAnneePrecedente(1L)).thenReturn(List.of(voeux));

        mockMvc.perform(put("/api/form/formulaire-choix")
                .param("professeurId", "1")
                .param("copierAnneePrecedente", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void testPreRemplirFormulaireAnneePrecedente_WhenNotChecked() throws Exception {
        mockMvc.perform(put("/api/form/formulaire-choix")
                .param("professeurId", "1")
                .param("copierAnneePrecedente", "false"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}