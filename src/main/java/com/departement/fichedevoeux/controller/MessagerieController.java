package com.departement.fichedevoeux.controller;

import com.departement.fichedevoeux.model.Message;
import com.departement.fichedevoeux.model.Conversation;
import com.departement.fichedevoeux.service.MessageService;
import com.departement.fichedevoeux.service.ConversationService;

import DTO.MessageRequestDTO;
import DTO.ConversationRequestDTO;

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
    @PostMapping("/conversations/create")
    public ResponseEntity<?> creerConversation(@RequestBody ConversationRequestDTO request) {
        boolean ok = conversationService.creerConversation(request);
        return ok ? ResponseEntity.ok("Conversation créée")
                  : ResponseEntity.badRequest().body("Échec création");
    }

    // Charger les conversations d’un prof
    @GetMapping("/conversations")
    public ResponseEntity<List<Conversation>> getConversations(@RequestParam Long profId) {
        return ResponseEntity.ok(conversationService.getConversationsParProf(profId));
    }

    // Envoyer un message
    @PostMapping("/messages/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequestDTO request) {
        boolean ok = messageService.envoyerMessage(request);
        return ok ? ResponseEntity.ok("Message envoyé")
                  : ResponseEntity.badRequest().body("Erreur d’envoi");
    }

    // Charger les messages d’une conversation
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages(@RequestParam Long conversationId) {
        return ResponseEntity.ok(messageService.getMessagesParConversation(conversationId));
    }
}