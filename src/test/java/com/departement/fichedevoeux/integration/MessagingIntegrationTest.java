package com.departement.fichedevoeux.integration;

import com.departement.fichedevoeux.FichedevoeuxApplication;
import com.departement.fichedevoeux.model.Conversation;
import com.departement.fichedevoeux.model.Departement;
import com.departement.fichedevoeux.model.Message;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.ConversationRepository;
import com.departement.fichedevoeux.repository.DepartementRepository;
import com.departement.fichedevoeux.repository.MessageRepository;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import com.departement.fichedevoeux.service.ConversationService;
import com.departement.fichedevoeux.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;

import DTO.ConversationRequestDTO;
import DTO.MessageRequestDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FichedevoeuxApplication.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc // 💡 Required to inject MockMvc
@Transactional
@WithMockUser(username = "Nom", roles = {"PROF"})
public class MessagingIntegrationTest {
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private ConversationRepository conversationRepository;
    
    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        conversationRepository.deleteAll();
        professeurRepository.deleteAll();
    }

    @Test
    public void testControllerUsesServiceAndSavesConversation() throws Exception {
    	
    	
    	
        // Create a Professeur to be used in the DTO
    	
    	Departement departement = new Departement("Informatique");	
    	departementRepository.save(departement);
    	
        Professeur prof = new Professeur();
        prof.setNom("Nom");
        prof.setPrenom("Prenom");
        prof.setEmail("durand@example.com");
        prof.setMotDePasse("someSecurePassword");
        prof.setDepartement(departement);
        professeurRepository.save(prof);

        // Create the DTO
        ConversationRequestDTO dto = new ConversationRequestDTO();
        dto.setInitiateurId(prof.getId());
        dto.setSujet("Nouvelle conversation via controller");

        // Perform request to controller (which will call the service)
        mockMvc.perform(post("/api/messagerie/conversations/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Conversation créée"));

        // Verify: it actually reached the database (meaning service worked)
        List<Conversation> all = conversationRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getSujet()).isEqualTo("Nouvelle conversation via controller");
        assertThat(all.get(0).getInitiateur().getId()).isEqualTo(prof.getId());
    }
    
}
