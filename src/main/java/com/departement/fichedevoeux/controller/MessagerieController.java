package com.departement.fichedevoeux.controller;

import com.departement.fichedevoeux.model.Message;
import com.departement.fichedevoeux.model.Conversation;
import com.departement.fichedevoeux.service.MessageService;
import com.departement.fichedevoeux.service.ConversationService;

import DTO.MessageRequestDTO;
import DTO.ConversationDTO;
import DTO.ConversationRequestDTO;
import DTO.MessageDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messagerie")
public class MessagerieController {

    private final MessageService messageService;
    private final ConversationService conversationService;

    public MessagerieController(MessageService messageService, ConversationService conversationService) {
        this.messageService = messageService;
        this.conversationService = conversationService;
    }

    // Créer une conversation
    //@PostMapping("/conversations/create")
    public ResponseEntity<?> creerConversation(@RequestBody ConversationRequestDTO request) {
    	System.out.println(">>> Controller reached"); // Debug 1
        boolean ok = conversationService.creerConversation(request);
        return ok ? ResponseEntity.ok("Conversation créée")
                  : ResponseEntity.badRequest().body("Échec création");
    }

    // Charger les conversations d’un prof
   // @GetMapping("/conversations")
    public ResponseEntity<List<Conversation>> getConversationss(@RequestParam Long profId) {
        return ResponseEntity.ok(conversationService.getConversationsParProf(profId));
    }

    // Envoyer un message
   // @PostMapping("/messages/send")
    public ResponseEntity<?> sendMessages(@RequestBody MessageRequestDTO request) {
        boolean ok = messageService.envoyerMessage(request);
        return ok ? ResponseEntity.ok("Message envoyé")
                  : ResponseEntity.badRequest().body("Erreur d’envoi");
    }

    // Charger les messages d’une conversation
   // @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessagess(@RequestParam Long conversationId) {
        return ResponseEntity.ok(messageService.getMessagesParConversation(conversationId));
    }
    
    @GetMapping("/conversations")
    public List<ConversationDTO> getConversations(@RequestParam Long profId) {
        return conversationService.getConversationsByProfId(profId);
    }

    @PostMapping("/conversations/create")
    public ResponseEntity<String> createConversation(@RequestBody ConversationRequestDTO dto) {
        boolean created = conversationService.creerConversation(dto);
        return created ? ResponseEntity.ok("Conversation créée") : ResponseEntity.badRequest().body("Erreur");
    }

    @GetMapping("/messages")
    public List<MessageDTO> getMessages(@RequestParam Long conversationId) {
        return messageService.getMessagesByConversationId(conversationId);
    }

    @PostMapping("/messages/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageRequestDTO dto) {
        boolean sent = messageService.envoyerMessage(dto);
        return sent ? ResponseEntity.ok("Message envoyé") : ResponseEntity.badRequest().body("Erreur");
    }
    
    @GetMapping("/by-conversation")
    public ResponseEntity<List<MessageDTO>> getMessagesByConversation(@RequestParam Long conversationId) {
        return ResponseEntity.ok(messageService.getMessagesByConversationIdSorted(conversationId));
    }
    
  

}