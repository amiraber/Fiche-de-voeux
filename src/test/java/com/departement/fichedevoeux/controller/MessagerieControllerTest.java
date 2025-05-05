package com.departement.fichedevoeux.controller;

import com.departement.fichedevoeux.config.TestSecurityConfig;
import com.departement.fichedevoeux.model.Conversation;
import com.departement.fichedevoeux.model.Message;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.service.ConversationService;
import com.departement.fichedevoeux.service.MessageService;

import DTO.ConversationRequestDTO;
import DTO.MessageRequestDTO;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(MessagerieController.class)
public class MessagerieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private ConversationService conversationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreerConversation_success() throws Exception {
        ConversationRequestDTO dto = new ConversationRequestDTO();
        dto.setInitiateurId(1L);
        dto.setSujet("Sujet test");

        when(conversationService.creerConversation(any(ConversationRequestDTO.class))).thenReturn(true);

        mockMvc.perform(post("/api/messagerie/conversations/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Conversation créée"));
    }

    @Test
    public void testCreerConversation_failure() throws Exception {
        ConversationRequestDTO dto = new ConversationRequestDTO();
        dto.setInitiateurId(1L);
        dto.setSujet("Sujet test");

        when(conversationService.creerConversation(any(ConversationRequestDTO.class))).thenReturn(false);

        mockMvc.perform(post("/api/messagerie/conversations/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Échec création"));
    }

    @Test
    public void testGetConversations() throws Exception {
        Professeur prof = new Professeur(); // Simulé
        prof.setId(1L);
        Conversation c = new Conversation("Sujet", "2024-04-01", prof);
        c.setIdConversation(10);

        List<Conversation> list = Arrays.asList(c);
        when(conversationService.getConversationsParProf(1L)).thenReturn(list);

        mockMvc.perform(get("/api/messagerie/conversations")
                .param("profId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idConversation").value(10));
    }

    @Test
    public void testSendMessage_success() throws Exception {
        MessageRequestDTO dto = new MessageRequestDTO();
        dto.setSenderId(1L);
        dto.setConversationId(2L);
        dto.setContent("Bonjour");

        when(messageService.envoyerMessage(any(MessageRequestDTO.class))).thenReturn(true);

        mockMvc.perform(post("/api/messagerie/messages/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Message envoyé"));
    }

    @Test
    public void testSendMessage_failure() throws Exception {
        MessageRequestDTO dto = new MessageRequestDTO();
        dto.setSenderId(1L);
        dto.setConversationId(2L);
        dto.setContent("Bonjour");

        when(messageService.envoyerMessage(any(MessageRequestDTO.class))).thenReturn(false);

        mockMvc.perform(post("/api/messagerie/messages/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur d’envoi"));
    }

    @Test
    public void testGetMessages() throws Exception {
        Professeur auteur = new Professeur();
        auteur.setId(1L);
        Conversation conv = new Conversation();
        conv.setIdConversation(2);

        Message msg = new Message(conv, auteur, "2025-04-29", "Hello", true);
        msg.setIdMessage(5);

        when(messageService.getMessagesParConversation(2L)).thenReturn(List.of(msg));

        mockMvc.perform(get("/api/messagerie/messages")
                .param("conversationId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMessage").value(5));
    }
}
