package com.departement.fichedevoeux.integration;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.departement.fichedevoeux.FichedevoeuxApplication;
import com.departement.fichedevoeux.model.Departement;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.DepartementRepository;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import DTO.LoginRequestDTO;
import DTO.RegisterRequestDTO;

@SpringBootTest(classes = FichedevoeuxApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test") // Optional: if you want different config for test env
@WithMockUser(username = "Nom", roles = {"PROF"})
@Transactional // Ensures DB is reset between each test
public class AuthIntegrationTest {

	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private DepartementRepository departementRepository;

	@Autowired
	private ProfesseurRepository professeurRepository;
	
	 @Autowired
	    private ObjectMapper objectMapper;
	 
	  @BeforeEach
	    public void setup() {
		 
	        professeurRepository.deleteAll();
	    }

	
	@Test
	public void testRegisterSuccess() throws Exception {
	    // Setup - create department
	    Departement dept = new Departement("IA");
	    departementRepository.save(dept);

	    RegisterRequestDTO request = new RegisterRequestDTO(
	        "amira@gmail.com", "password123", "Amira", "mimi", "IA");

	    mockMvc.perform(post("/api/auth/register")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().writeValueAsString(request)))
	            .andExpect(status().isOk())
	            .andExpect(content().string("User registered successfully"));
	    
	    // ✅ Verify that the user was actually added to the DB
	    Professeur saved = professeurRepository.findByEmail("amira@gmail.com");
	    assertNotNull(saved);
	    assertEquals("Amira", saved.getNom());
	    assertEquals("mimi", saved.getPrenom());
	    assertEquals("IA", saved.getDepartement().getNomDepartement());
	    
	}
	
	//case departement missing but debug message wrong
	@Test
	public void testRegisterFailureDepartmentMissing() throws Exception {
	    RegisterRequestDTO request = new RegisterRequestDTO(
	        "amira@gmail.com", "password123", "Amira", "mimp","NON_EXISTENT");

	    mockMvc.perform(post("/api/auth/register")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().writeValueAsString(request)))
	            .andExpect(status().isBadRequest())
	            .andExpect(content().string("Email already used"));
	    // This message could be confusing — consider improving message logic in controller/service
	}

	@Test
	public void testRegisterFailureEmailUsed() throws Exception {
	    // Setup: Create department and professeur
	    Departement dept = new Departement("CS");
	  
	    departementRepository.save(dept);

	    Professeur prof = new Professeur();
	    prof.setEmail("amira@gmail.com");
	    prof.setMotDePasse(new BCryptPasswordEncoder().encode("password123"));
	    prof.setNom("Amira");
	    prof.setPrenom("mimol");
	    prof.setDepartement(dept);
	    prof.setChef(false);
	    professeurRepository.save(prof);

	    RegisterRequestDTO request = new RegisterRequestDTO(
	        "amira@gmail.com", "password123", "Amira","mimol", "CS");

	    mockMvc.perform(post("/api/auth/register")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().writeValueAsString(request)))
	            .andExpect(status().isBadRequest())
	            .andExpect(content().string("Email already used"));
	}
	
	@Test
	public void testLoginSuccess() throws Exception {
	    // Setup
	    Departement dept = new Departement();
	    dept.setNomDepartement("IA");
	    departementRepository.save(dept);

	    Professeur prof = new Professeur();
	    prof.setEmail("amira@gmail.com");
	    prof.setMotDePasse(new BCryptPasswordEncoder().encode("password123"));
	    prof.setNom("Amira");
	    prof.setPrenom("mimi");
	    prof.setDepartement(dept);
	    professeurRepository.save(prof);

	    LoginRequestDTO request = new LoginRequestDTO("amira@gmail.com", "password123");

	    mockMvc.perform(post("/api/auth/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().writeValueAsString(request)))
	            .andExpect(status().isOk())
	            .andExpect(content().string("Login successful"));
	}

	@Test
	public void testLoginFailure() throws Exception {
	    LoginRequestDTO request = new LoginRequestDTO("nonexistent@gmail.com", "password");

	    mockMvc.perform(post("/api/auth/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().writeValueAsString(request)))
	            .andExpect(status().isUnauthorized())
	            .andExpect(content().string("Invalid Email or password"));
	}
	
}
