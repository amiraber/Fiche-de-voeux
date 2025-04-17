package com.departement.fichedevoeux.repository;

import com.departement.fichedevoeux.model.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {
}
