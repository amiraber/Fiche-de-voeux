package com.departement.fichedevoeux.repository;

import com.departement.fichedevoeux.model.Conversation;
import com.departement.fichedevoeux.model.Message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Integer> {
	List<Message> findByAuteurId(Long id);
	List<Message> findByConversationIdConversation(Integer idConversation);
	void deleteAllByConversation(Conversation conversation);
	List<Message> findByConversation_IdConversation(Integer conversationId);
	
	@Query("SELECT m FROM Message m WHERE m.conversation.idConversation = :conversationId ORDER BY m.dateEnvoi ASC")
	List<Message> findByConversationIdOrderByDateAsc(@Param("conversationId") Integer conversationId);
}
