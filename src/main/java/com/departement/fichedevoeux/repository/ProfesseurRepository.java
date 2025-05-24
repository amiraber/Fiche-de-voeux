package com.departement.fichedevoeux.repository;

import com.departement.fichedevoeux.model.Professeur;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {
	Professeur findByEmail(String email);
	List<Professeur> findByNom(String nom);
	List<Professeur> findByPrenom(String prenom);
	List<Professeur> findByDepartementId(Long id);
	List<Professeur> findByIsChefTrue();
	@Query("SELECT p FROM Professeur p WHERE p.id IN :ids AND p.departement.id = :departementId")
	List<Professeur> findByIdInAndDepartementId(@Param("ids") List<Long> ids, @Param("departementId") Long departementId);
	@Query("SELECT p FROM Professeur p WHERE p.departement.id = :departementId AND p.isChef = true")
	Professeur findChefByDepartement(@Param("departementId") Long departementId);

}
