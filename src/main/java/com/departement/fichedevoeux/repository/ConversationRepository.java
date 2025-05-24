package com.departement.fichedevoeux.repository;

import com.departement.fichedevoeux.model.Conversation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
	List<Conversation> findByInitiateurId(Long id);
	
	@Query("SELECT c FROM Conversation c " +
		       "JOIN Message m ON m.conversation.idConversation = c.idConversation " +
		       "WHERE c.initiateur.id = :chefId AND m.auteur.id = :profId")
		Optional<Conversation> findByChefAndProf(@Param("chefId") Long chefId, @Param("profId") Long profId);
	
	@Query("SELECT c FROM Conversation c JOIN c.messages m WHERE (m.auteur.id = :profId AND c.initiateur.id = :chefId) OR (m.auteur.id = :chefId AND c.initiateur.id = :profId)")
	Optional<Conversation> findByParticipants(@Param("profId") Long profId, @Param("chefId") Long chefId);

	
	@Query("""
			SELECT DISTINCT c FROM Conversation c
			WHERE 
			  (c.initiateur.id = :chefId AND EXISTS (
			      SELECT m FROM Message m WHERE m.conversation.id = c.id AND m.auteur.id = :profId
			  ))
			  OR 
			  (c.initiateur.id = :profId AND EXISTS (
			      SELECT m FROM Message m WHERE m.conversation.id = c.id AND m.auteur.id = :chefId
			  ))
			""")
			Optional<Conversation> findExistingConversation(@Param("profId") Long profId, @Param("chefId") Long chefId);
}
