package com.departement.fichedevoeux.service;

import com.departement.fichedevoeux.model.Conversation;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.ConversationRepository;
import com.departement.fichedevoeux.repository.ProfesseurRepository;

import DTO.ConversationDTO;
import DTO.ConversationRequestDTO;

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
public class ConversationService {
	
	private static final Logger log = LoggerFactory.getLogger(ConversationService.class);

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    // Créer une conversation
    //utilise pas!
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
    //utilise pas
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
    
    public List<ConversationDTO> getConversationsByProfId(Long profId) {
        return conversationRepository.findByInitiateurId(profId).stream().map(conv -> {
            ConversationDTO dto = new ConversationDTO();
            dto.setIdConversation(conv.getIdConversation());
            dto.setSujet(conv.getSujet());
            String dateFormatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            dto.setDateCreation(dateFormatted);
            dto.setInitiateurNom(conv.getInitiateur().getNom());
            return dto;
        }).collect(Collectors.toList());
    }

    public Integer getOrCreateConversationId(Long profId, Long chefId) {
        Optional<Conversation> existing = conversationRepository.findByChefAndProf(chefId, profId);
        if (existing.isPresent()) {
            return existing.get().getIdConversation(); // ✅ Elle existe déjà
        }

        Professeur chef = professeurRepository.findById(chefId).orElseThrow(); // 👨‍🏫 le chef

        Conversation conv = new Conversation();
        conv.setSujet("Conversation avec professeur " + profId); // 🗣️ le titre
        conv.setDateCreation(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))); // 🕒 la date
        conv.setInitiateur(chef); // 🙋 le chef qui commence

        conversationRepository.save(conv); // 💾 on enregistre
        return conv.getIdConversation(); // 🔢 on renvoie le numéro
    }
    
    public Integer getExistingConversationId(Long profId, Long chefId) {
        return conversationRepository
                .findExistingConversation(profId, chefId)
                .map(Conversation::getIdConversation)
                .orElse(null);
    }

}

