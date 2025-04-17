package com.departement.fichedevoeux.repository;

import com.departement.fichedevoeux.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
