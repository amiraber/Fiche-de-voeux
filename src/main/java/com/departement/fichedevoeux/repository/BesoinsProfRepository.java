package com.departement.fichedevoeux.repository;

import com.departement.fichedevoeux.model.BesoinsProf;
import com.departement.fichedevoeux.model.Professeur;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BesoinsProfRepository extends JpaRepository<BesoinsProf, Long> {
	List<BesoinsProf> findByProfesseurId(Long id);
}
