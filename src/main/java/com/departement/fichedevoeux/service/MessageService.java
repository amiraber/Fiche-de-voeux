package com.departement.fichedevoeux.service;

import com.departement.fichedevoeux.model.Conversation;
import com.departement.fichedevoeux.model.Message;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.ConversationRepository;
import com.departement.fichedevoeux.repository.MessageRepository;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import DTO.MessageRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MessageService {
	
	private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    // ✅ Envoyer un message dans une conversation
    public boolean envoyerMessage(MessageRequestDTO dto) {
    	
    	log.info(">>>>>SERVICE MESSAGE SEND MESSAGE METHOD REACHED ✅");
    	
        if (dto == null || dto.getSenderId() == null || dto.getConversationId() == null || dto.getContent() == null || dto.getContent().isEmpty()) {
            return false;
        }

        Professeur sender = professeurRepository.findById(dto.getSenderId()).orElse(null);
        Conversation conversation = conversationRepository.findById(dto.getConversationId().intValue()).orElse(null);

        if (sender == null || conversation == null) {
            return false;
        }

        Message message = new Message();
        message.setAuteur(sender);
        message.setConversation(conversation);
        message.setContenu(dto.getContent());
        message.setEstLu(false); // message non lu par défaut
        message.setDateEnvoi(LocalDateTime.now().toString()); 

        messageRepository.save(message);
        return true;
    }

    // ✅ Charger tous les messages d’une conversation
    public List<Message> getMessagesParConversation(Long conversationId) {
    	log.info(">>>>>SERVICE MESSAGE GET MESSAGE METHOD REACHED ✅");
        if (conversationId == null) return List.of();
        
        List<Message> list = messageRepository.findByConversationIdConversation(conversationId.intValue());
        log.info("✅ Messages found in service: {}", list);
        return list;
       
        //return messageRepository.findByConversationIdConversation(conversationId.intValue());
    }
}