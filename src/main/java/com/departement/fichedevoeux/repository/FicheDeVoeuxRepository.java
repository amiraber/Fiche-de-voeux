package com.departement.fichedevoeux.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.departement.fichedevoeux.model.Voeux;

public interface FicheDeVoeuxRepository extends JpaRepository<Voeux, Long> {
	List<Voeux> findAll();
	List<Voeux> findByProfesseurId(Long profId);
	List<Voeux> findByProfesseurIdAndAnnee(Long profId, int annee);
	List<Voeux> findByAnnee(int annee);
	void deleteByProfesseurId(Long idProf);
	@Query("SELECT DISTINCT v.professeur.id FROM Voeux v")
	List<Long> findDistinctProfesseurIds();
	@Query("SELECT v FROM Voeux v JOIN FETCH v.module WHERE v.professeur.id = :profId")
	List<Voeux> findWithModuleByProfesseurId(@Param("profId") Long profId);

	

	
}
