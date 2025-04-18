package com.departement.fichedevoeux.repository;

import com.departement.fichedevoeux.model.Departement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartementRepository extends JpaRepository<Departement, Long> {
	Departement findByNomDepartement(String nomDepartement);
	List<Departement> findByNomDepartementContainingIgnoreCase(String nomDepartement);
}
