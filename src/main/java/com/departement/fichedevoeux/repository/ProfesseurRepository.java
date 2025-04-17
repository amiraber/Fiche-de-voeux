package com.departement.fichedevoeux.repository;

import com.departement.fichedevoeux.model.Professeur;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {
	Professeur findByEmail(String email);
	List<Professeur> findByNom(String nom);
	List<Professeur> findByDepartementId(Long id);
	List<Professeur> findByIsChefTrue();



}
