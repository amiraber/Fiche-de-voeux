package com.departement.fichedevoeux.service;

import com.departement.fichedevoeux.model.Message;
import com.departement.fichedevoeux.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


public class MessageService {

@Autowired 
private MessageRepository messageRepository;
	public Message sendMessage(Message message)  {
		message.setDateEnvoi(LocalDateTime.now().toString());
		return messageRepository.save(message);
	}
	
	public List<Message> getMessageByAuteurId(Long auteurId) {
		return messageRepository.findByAuteurId(auteurId);
	}
	
	
}
