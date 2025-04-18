package com.departement.fichedevoeux.repository;

import com.departement.fichedevoeux.model.Conversation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
	List<Conversation> findByInitiateurId(Long id);
}
