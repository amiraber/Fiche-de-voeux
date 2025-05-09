package com.departement.fichedevoeux.repository;
import com.departement.fichedevoeux.model.Module;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository <Module, Long> {
	
	// Filtrer les modules en fonction du semestre, pallier et spécialité
    List<Module> findBySemestreAndPallierAndSpecialite(String semestre, String pallier, String specialite);

    // Obtenir la liste distincte des spécialités en fonction du semestre et pallier
    @Query("SELECT DISTINCT m.specialite FROM Module m WHERE m.semestre = :semestre AND m.pallier = :pallier")
    List<String> findDistinctSpecialitesBySemestreAndPallier(@Param("semestre") String semestre, @Param("pallier") String pallier);

    // rechercher des modules par leurs noms
	List<Module> findByNomIn(List<String> noms);
}
