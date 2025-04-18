package com.departement.fichedevoeux.repository;


import com.departement.fichedevoeux.model.Voeux;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VoeuxRepository extends JpaRepository<Voeux, Integer> {
	 List<Voeux> findByProfesseurId(Long id);

	    List<Voeux> findBySemestre(Integer semestre);

	    List<Voeux> findByModuleId(Long id);
}