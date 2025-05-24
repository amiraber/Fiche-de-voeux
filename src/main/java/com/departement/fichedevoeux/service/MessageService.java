package com.departement.fichedevoeux.service;

import com.departement.fichedevoeux.model.Conversation;
import com.departement.fichedevoeux.model.Message;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.ConversationRepository;
import com.departement.fichedevoeux.repository.MessageRepository;
import com.departement.fichedevoeux.repository.ProfesseurRepository;

import DTO.MessageDTO;
import DTO.MessageRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    //pas
    public boolean envoyerMessaged(MessageRequestDTO dto) {
    	
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
    //pas
    public List<Message> getMessagesParConversation(Long conversationId) {
    	log.info(">>>>>SERVICE MESSAGE GET MESSAGE METHOD REACHED ✅");
        if (conversationId == null) return List.of();
        
        List<Message> list = messageRepository.findByConversationIdConversation(conversationId.intValue());
        log.info("✅ Messages found in service: {}", list);
        return list;
       
        //return messageRepository.findByConversationIdConversation(conversationId.intValue());
    }
    
    public List<MessageDTO> getMessagesByConversationId(Long conversationId) {
    	return messageRepository.findByConversation_IdConversation(conversationId.intValue())
    		    .stream().map(msg -> {
    		        MessageDTO dto = new MessageDTO();
    		        dto.setContenu(msg.getContenu());
    		        dto.setSenderId(msg.getAuteur().getId());
    		        dto.setLu(msg.isEstLu());
    		        return dto;
    		    }).collect(Collectors.toList());

    }

   //envoy
    public boolean envoyerMessage(MessageRequestDTO dto) {
        if (dto == null || dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            return false;
        }

        Professeur sender = professeurRepository.findById(dto.getSenderId()).orElse(null);
        if (sender == null) return false;

        Conversation conv = null;

        // Soit une conversation est déjà connue (par l'interface)
        if (dto.getConversationId() != null) {
            conv = conversationRepository.findById(dto.getConversationId().intValue()).orElse(null);
        }

        // Soit il faut la retrouver ou la créer
        if (conv == null) {
            // Recherche bidirectionnelle
            Optional<Conversation> existing = conversationRepository.findByChefAndProf(sender.getId(), null);
            if (existing.isEmpty()) {
                existing = conversationRepository.findByChefAndProf(null, sender.getId());
            }

            if (existing.isPresent()) {
                conv = existing.get();
            } else {
                // Création si pas trouvé
                conv = new Conversation();
                conv.setSujet("Conversation ouverte par " + sender.getNom());
                conv.setDateCreation(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                conv.setInitiateur(sender);
                conversationRepository.save(conv);
            }
        }

        if (conv == null) return false;

        Message msg = new Message();
        msg.setAuteur(sender);
        msg.setConversation(conv);
        msg.setContenu(dto.getContent());
        msg.setDateEnvoi(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        msg.setEstLu(false);

        messageRepository.save(msg);
        return true;
    }

    public List<MessageDTO> getMessagesByConversationIdSorted(Long conversationId) {
        return messageRepository.findByConversationIdOrderByDateAsc(conversationId.intValue())
                .stream().map(msg -> {
                    MessageDTO dto = new MessageDTO();
                    dto.setContenu(msg.getContenu());
                    dto.setSenderId(msg.getAuteur().getId());
                    dto.setLu(msg.isEstLu());
                    dto.setTypeExpediteur(msg.getAuteur().isChef() ? "ADMIN" : "PROF");
                    return dto;
                }).collect(Collectors.toList());
    }
    
  

}