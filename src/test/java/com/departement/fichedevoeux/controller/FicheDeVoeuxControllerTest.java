package com.departement.fichedevoeux.controller;

import com.departement.fichedevoeux.config.TestSecurityConfig;
import com.departement.fichedevoeux.model.Module;
import com.departement.fichedevoeux.service.FicheDeVoeuxService;
import DTO.FormulaireRequestDTO;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FicheDeVoeuxController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class FicheDeVoeuxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FicheDeVoeuxService ficheDeVoeuxService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSubmitForm_Success() throws Exception {
        FormulaireRequestDTO form = new FormulaireRequestDTO();
        form.setProfesseurId(1L);

        when(ficheDeVoeuxService.submitFormulaire(any())).thenReturn(true);

        mockMvc.perform(post("/api/form/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().string("Formulaire soumis avec succès."));
    }

    @Test
    public void testCheckStatus() throws Exception {
        when(ficheDeVoeuxService.hasSubmitted(1L)).thenReturn(true);

        mockMvc.perform(get("/api/form/status").param("professeurId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Statut : Soumis"));
    }

    @Test
    public void testGetModulesFiltres() throws Exception {
        Module m = new Module("L1", "Info", "S1", "POO");
        when(ficheDeVoeuxService.filterModules("S1", "L1", "Info")).thenReturn(List.of(m));

        mockMvc.perform(get("/api/form/modules")
                .param("semestre", "S1")
                .param("pallier", "L1")
                .param("specialite", "Info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("POO"));
    }
}
