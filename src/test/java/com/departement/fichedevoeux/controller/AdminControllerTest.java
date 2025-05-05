package com.departement.fichedevoeux.controller;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.departement.fichedevoeux.config.TestSecurityConfig;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import com.departement.fichedevoeux.service.AdminService;


import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private ProfesseurRepository professeurRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"CHEF"})
    public void testExportExcel_AsChef_ShouldReturnFile() throws Exception {
        Long profId = 1L;
        byte[] fakeFile = "fake-excel".getBytes();

        when(adminService.isChef(profId)).thenReturn(true);
        when(adminService.exporterExcel()).thenReturn(fakeFile);

        mockMvc.perform(get("/api/admin/export").param("profId", profId.toString()))
               .andExpect(status().isOk())
               .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=voeux.xlsx"))
               .andExpect(content().bytes(fakeFile));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"CHEF"})
    public void testExportExcel_NotChef_ShouldReturn403() throws Exception {
        when(adminService.isChef(1L)).thenReturn(false);

        mockMvc.perform(get("/api/admin/export"))
               .andExpect(status().isForbidden());
               //.andExpect(content().string(containsString("Access denied")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"CHEF"})
    public void testGetDeadlineStatus_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/admin/deadline"))
               .andExpect(status().isOk())
               .andExpect(content().string("Deadline is active"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"CHEF"})
    public void testSetDeadline_AsChef_ShouldReturnOk() throws Exception {
        Long profId = 1L;
        Professeur chef = new Professeur();
        chef.setChef(true);
        
        when(professeurRepository.findById(profId)).thenReturn(Optional.of(chef));
        when(adminService.setDeadline(any())).thenReturn(true);

        String json = """
        {
            "deadline": "2025-05-31T23:59:00"
        }
        """;

        mockMvc.perform(post("/api/admin/deadline")
                .param("profId", profId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Deadline updated"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"CHEF"})
    public void testSetDeadline_NotChef_ShouldReturn403() throws Exception {
        Long profId = 2L;
        Professeur nonchef = new Professeur();
        nonchef.setChef(false);
        
        when(professeurRepository.findById(profId)).thenReturn(Optional.of(nonchef));

        String json = """
        {
            "deadline": "2025-05-31T23:59:00"
        }
        """;

        mockMvc.perform(post("/api/admin/deadline")
                .param("profId", profId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isForbidden());
                //.andExpect(content().string(containsString("Access denied")));
    }
}
