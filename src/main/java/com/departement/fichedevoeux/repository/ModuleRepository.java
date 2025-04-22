package com.departement.fichedevoeux.repository;
import com.departement.fichedevoeux.model.Module;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository <Module, Integer> {
	List <Module> findByNomContainingIgnoreCase(String nom);
	List<Module> findBySemestre(String semestre);
	List <Module> findByPallier(String pallier);
	List<Module> findByPallierAndSpecialite(String pallier, String specialite);
}
