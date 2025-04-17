package com.departement.fichedevoeux.controller;

import com.departement.fichedevoeux.model.Message;
import com.departement.fichedevoeux.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/messages")

public class MessageController {
	@Autowired
	private MessageService messageService;
	
	 @PostMapping
	 public Message sendMessage(@RequestBody Message message) {
		 return messageService.sendMessage(message);
	 }
	 @GetMapping
	 public List <Message> getMessageByUser(@RequestParam("user") Long userId) {
		 return messageService.getMessageByAuteurId(userId);
	 }
}
