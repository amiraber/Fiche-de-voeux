package com.departement.fichedevoeux.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.departement.fichedevoeux.FichedevoeuxApplication;
import com.departement.fichedevoeux.config.TestSecurityConfig;
import com.departement.fichedevoeux.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import DTO.LoginRequestDTO;
import DTO.RegisterRequestDTO;

@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)

public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;
    @Test
    public void testRegisterSuccess() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("amira@gmail.com", "password123", "Berrrabah Amira","momi", "IA");

        when(authService.register(any(RegisterRequestDTO.class))).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    public void testRegisterEmailUsed() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("amira@gmail.com", "password123", "Amira", "momi","CS");

        when(authService.register(any(RegisterRequestDTO.class))).thenReturn(false);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already used"));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("amira@gmail.com", "password123");

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    public void testLoginFailure() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("amira@gmail.com", "wrongpassword");

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid Email or password"));
    }
    
   
}
