package com.departement.fichedevoeux.service;

import com.departement.fichedevoeux.model.Conversation;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.ConversationRepository;
import com.departement.fichedevoeux.repository.ProfesseurRepository;
import DTO.ConversationRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ConversationService {
	
	private static final Logger log = LoggerFactory.getLogger(ConversationService.class);

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    // Créer une conversation
    public boolean creerConversation(ConversationRequestDTO dto) {
    	
    	
    	

    	log.info(">>>>>SERVICE METHOD REACHED ✅");

   
        if (dto.getInitiateurId() == null || dto.getSujet() == null || dto.getSujet().isEmpty()) {
            return false;
        }

        Professeur initiateur = professeurRepository.findById(dto.getInitiateurId()).orElse(null);
        if (initiateur == null) return false;

        Conversation conversation = new Conversation();
        conversation.setInitiateur(initiateur);
        conversation.setSujet(dto.getSujet());

        // Ajoute automatiquement la date de création
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        conversation.setDateCreation(LocalDateTime.now().format(formatter));

        conversationRepository.save(conversation);
        return true;
    }

    // Charger toutes les conversations d’un prof
    public List<Conversation> getConversationsParProf(Long profId) {
    	
    	log.info(">>>>>SERVICE METHOD REACHED for get conversation ✅");
    	
        if (profId == null) {
        	
        	log.info("pas de conversation");
        	return List.of();
        }
      //  return conversationRepository.findByInitiateurId(profId);
        List<Conversation> list = conversationRepository.findByInitiateurId(profId);
        log.info("🟢 Conversations found for Prof ID {}: {}", profId, list);
        return list;
    }
}

