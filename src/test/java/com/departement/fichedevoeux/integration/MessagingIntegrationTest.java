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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootTest(classes = FichedevoeuxApplication.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc // 💡 Required to inject MockMvc
@Transactional
@WithMockUser(username = "Nom", roles = {"PROF"})
public class MessagingIntegrationTest {
	
	private static final Logger log = LoggerFactory.getLogger(MessagingIntegrationTest.class);
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private ConversationRepository conversationRepository;
    
    @Autowired
    private DepartementRepository departementRepository;
    
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        conversationRepository.deleteAll();
        professeurRepository.deleteAll();
    }
    
    //test des converstaions

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
    
    
    @Test
    public void testCreerConversation_failure() throws Exception {
    	
    	
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
        dto.setInitiateurId(null);
        dto.setSujet("Nouvelle conversation via controller");
    	
       
       //perform request tocontroller with real service
        mockMvc.perform(post("/api/messagerie/conversations/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Échec création"));
        
     // Optionally: make sure nothing was saved
        List<Conversation> all = conversationRepository.findAll();
        assertThat(all).isEmpty();
    }
    
    @Test
    public void testGetConversations() throws Exception {
    	
    	Departement departement = new Departement("Informatique");	
    	departementRepository.save(departement);
    	
    	 Professeur prof = new Professeur();
         prof.setNom("Nom");
         prof.setPrenom("Prenom");
         prof.setEmail("durand@example.com");
         prof.setMotDePasse("someSecurePassword");
         prof.setDepartement(departement);
         professeurRepository.save(prof);
         
         // Create and save a Conversation
         Conversation conv = new Conversation();
         conv.setSujet("Sujet testé");
         conv.setDateCreation("2024-04-01");
         conv.setInitiateur(prof);
         conversationRepository.save(conv);
        

        mockMvc.perform(get("/api/messagerie/conversations")
        		.param("profId", prof.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idConversation").value(conv.getIdConversation()));
    }
    
    //tests des messages
    
    @Test
    @Transactional
    public void testSendMessage_success() throws Exception {
        // Setup: create real data
        Departement departement = new Departement("Mathématiques");
        departementRepository.save(departement);

        Professeur prof = new Professeur();
        prof.setNom("Doe");
        prof.setPrenom("John");
        prof.setEmail("john.doe@example.com");
        prof.setMotDePasse("secure123");
        prof.setDepartement(departement);
        professeurRepository.save(prof);

        Conversation conversation = new Conversation();
        conversation.setSujet("Sujet Important");
        conversation.setDateCreation("2024-05-01");
        conversation.setInitiateur(prof);
        conversationRepository.save(conversation);

        // Build DTO
        MessageRequestDTO dto = new MessageRequestDTO();
        dto.setSenderId(prof.getId());
        dto.setConversationId((long) conversation.getIdConversation());
        dto.setContent("Bonjour, c'est un message de test.");

        // Perform POST request
        mockMvc.perform(post("/api/messagerie/messages/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Message envoyé"));

        // Optional: Verify the message was saved
        List<Message> messages = messageRepository.findAll();
        assertFalse(messages.isEmpty());
        assertEquals("Bonjour, c'est un message de test.", messages.get(0).getContenu());

    }
    
    @Test
    public void testSendMessage_failure() throws Exception {
    	
    	 // Setup: create real data
        Departement departement = new Departement("Mathématiques");
        departementRepository.save(departement);

        Professeur prof = new Professeur();
        prof.setNom("Doe");
        prof.setPrenom("John");
        prof.setEmail("john.doe@example.com");
        prof.setMotDePasse("secure123");
        prof.setDepartement(departement);
        professeurRepository.save(prof);

        Conversation conversation = new Conversation();
        conversation.setSujet("Sujet Important");
        conversation.setDateCreation("2024-05-01");
        conversation.setInitiateur(prof);
        conversationRepository.save(conversation);

        // Build DTO
        MessageRequestDTO dto = new MessageRequestDTO();
        dto.setSenderId(null);
        dto.setConversationId((long) conversation.getIdConversation());
        dto.setContent("Bonjour, c'est un message de test.");
        
        mockMvc.perform(post("/api/messagerie/messages/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur d’envoi"));
    }

    
    @Test
    @Transactional
    public void testGetMessages_fullIntegration() throws Exception {
    	
    	 Departement departement = new Departement("Mathématiques");
         departementRepository.save(departement);
         
        // Create and save a Professeur
        Professeur auteur = new Professeur();
        auteur.setNom("Nom");
        auteur.setPrenom("Prenom");
        auteur.setEmail("auteur@example.com");
        auteur.setMotDePasse("securePass");
        auteur.setDepartement(departement);
        professeurRepository.save(auteur);

        // Create and save a Conversation
        Conversation conv = new Conversation();
        conv.setSujet("Sujet de test");
        conv.setDateCreation("2025-04-29");
        conv.setInitiateur(auteur);
        conversationRepository.save(conv);

        // Create and save a Message
        Message msg = new Message();
        msg.setAuteur(auteur);
        msg.setConversation(conv);
        msg.setDateEnvoi("2025-04-29");
        msg.setContenu("Hello");
        msg.setEstLu(true);
        messageRepository.save(msg);

        // Log to confirm test setup is working
        List<Message> foundMessages = messageRepository.findByConversationIdConversation(conv.getIdConversation());
        log.info("✅ Messages found in test: {}", foundMessages);

        // Perform GET request
        mockMvc.perform(get("/api/messagerie/messages")
                .param("conversationId", String.valueOf(conv.getIdConversation())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMessage").value(msg.getIdMessage()));
    }

    
}
