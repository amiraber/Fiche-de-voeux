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

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    // Créer une conversation
    public boolean creerConversation(ConversationRequestDTO dto) {
    	
    	
    	System.out.println(">>> Service layer reached: creating conversation");
   
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
        if (profId == null) return List.of();
        return conversationRepository.findByInitiateurId(profId);
    }
}

