package com.departement.fichedevoeux.repository;


import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.model.Voeux;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VoeuxRepository extends JpaRepository<Voeux, Integer> {
	 	List<Voeux> findByProfesseurId(Long id);

	 	List<Voeux> findBySemestre(String semestre);

	    
	    List<Voeux> findByModuleIdModule(Long idModule);

		List<Voeux> findByProfesseurIdAndAnnee(Long id, int annee);
		boolean existsByProfesseurAndModuleAndNatureAndSemestreAndNumChoix(
			    Professeur prof, com.departement.fichedevoeux.model.Module module, String nature, String semestre, int numChoix
			);

}