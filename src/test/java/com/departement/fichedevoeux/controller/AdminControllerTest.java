package com.departement.fichedevoeux.controller;

import com.departement.fichedevoeux.config.TestSecurityConfig;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.service.AdminService;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import DTO.DeadlineRequestDTO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@ActiveProfiles("test")  // ✅ This activates TestSecurityConfig
@Import(TestSecurityConfig.class)  // ✅ Import TestSecurityConfig to ensure correct security settings during tests
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private ProfesseurRepository professeurRepository;

    @MockBean
    private AuthController authController; // Injected in constructor

    @Autowired
    private ObjectMapper objectMapper;

    private Professeur chefProf;

    @BeforeEach
    void setup() {
        chefProf = new Professeur();
        chefProf.setId(1L);
        chefProf.setChef(true);  // Set the "chef" attribute to true
    }

    // Test 1: Export Excel when user is CHEF
    @Test
    @WithMockUser(roles = "CHEF")
    void exportExcel_ShouldReturnFile_WhenUserIsChef() throws Exception {
        byte[] dummyFile = "fake excel content".getBytes();

        // Mock service response
        when(adminService.isChef(1L)).thenReturn(true);
        when(adminService.exporterExcel()).thenReturn(dummyFile);

        // Perform the test request and assertions
        mockMvc.perform(get("/api/admin/export")
                .param("profId", "1"))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", "attachment; filename=voeux.xlsx\""))
            .andExpect(content().bytes(dummyFile));  // Check the content
    }

    // Test 2: Export Excel should return Forbidden when user is not CHEF
    @Test
    @WithMockUser(roles = "CHEF")
    void exportExcel_ShouldReturnForbidden_WhenUserIsNotChef() throws Exception {
        // Mock service response
        when(adminService.isChef(2L)).thenReturn(false);

        // Perform the test request and assertions
        mockMvc.perform(get("/api/admin/export")
                .param("profId", "2"))
            .andExpect(status().isForbidden())
            .andExpect(content().string("Access denied: not a department head"));
    }

    // Test 3: Get Deadline Status when user is CHEF
    @Test
    @WithMockUser(roles = "CHEF")
    void getDeadlineStatus_ShouldReturnActive() throws Exception {
        mockMvc.perform(get("/api/admin/deadline"))
            .andExpect(status().isOk())
            .andExpect(content().string("Deadline is active"));
    }

    // Test 4: Set Deadline should update when user is CHEF
    @Test
    @WithMockUser(roles = "CHEF")
    void setDeadline_ShouldUpdate_WhenUserIsChef() throws Exception {
        DeadlineRequestDTO request = new DeadlineRequestDTO();
        request.setDeadline(LocalDate.of(2025, 6, 30));

        // Mock service response
        when(professeurRepository.findById(1L)).thenReturn(java.util.Optional.of(chefProf));
        when(adminService.setDeadline(request.getDeadline())).thenReturn(true);

        // Perform the test request and assertions
        mockMvc.perform(post("/api/admin/deadline")
                .param("profId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().string("Deadline updated"));
    }

    // Test 5: Set Deadline should return Forbidden when user is not CHEF
    @Test
    @WithMockUser(roles = "CHEF")
    void setDeadline_ShouldReturnForbidden_WhenUserIsNotChef() throws Exception {
        Professeur notChef = new Professeur();
        notChef.setId(2L);
        notChef.setChef(false);  // Set the "chef" attribute to false

        DeadlineRequestDTO request = new DeadlineRequestDTO();
        request.setDeadline(LocalDate.of(2025, 6, 30));

        // Mock service response
        when(professeurRepository.findById(2L)).thenReturn(java.util.Optional.of(notChef));

        // Perform the test request and assertions
        mockMvc.perform(post("/api/admin/deadline")
                .param("profId", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden())
            .andExpect(content().string("Access denied: not a department head"));
    }
}
