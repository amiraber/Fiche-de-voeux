package com.departement.fichedevoeux.repository;

import com.departement.fichedevoeux.model.Conversation;
import com.departement.fichedevoeux.model.Message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
	List<Message> findByAuteurId(Long id);
	List<Message> findByConversationIdConversation(Integer idConversation);
	void deleteAllByConversation(Conversation conversation);
}
