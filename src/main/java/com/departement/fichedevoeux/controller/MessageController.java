package com.departement.fichedevoeux.controller;

import com.departement.fichedevoeux.model.Message;
import com.departement.fichedevoeux.service.MessageService;

import DTO.MessageRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/messages")

public class MessageController {
	private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }
    //professors/admin can send messages.
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequestDTO request) {
        return ResponseEntity.ok("Message sent");
    }

    //loads inbox for a given professor
    @GetMapping("/inbox")
    public ResponseEntity<?> getMessages(@RequestParam Long profId) {
        return ResponseEntity.ok("Inbox loaded");
    }
}
