package com.departement.fichedevoeux.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.departement.fichedevoeux.model.Voeux;

public interface FicheDeVoeuxRepository extends JpaRepository<Voeux, Long> {
	List<Voeux> findAll();
	//List<Voeux> findByProfesseur();
	//List<Voeux> findByAnnee();
	// si plus tard on veut avoir les fiches des années précédentes, on rajoute l'attribut Annee dans Voeux et on repmlace findAll
	// par findByProfesseur et FindByAnnee 
}
