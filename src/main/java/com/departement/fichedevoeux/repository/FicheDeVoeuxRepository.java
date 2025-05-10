package com.departement.fichedevoeux.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.departement.fichedevoeux.model.Voeux;

public interface FicheDeVoeuxRepository extends JpaRepository<Voeux, Long> {
	List<Voeux> findAll();
	List<Voeux> findByProfesseurId(Long profId);
	List<Voeux> findByProfesseurIdAndAnnee(Long profId, int annee);
	List<Voeux> findByAnnee(int annee);
	
}
