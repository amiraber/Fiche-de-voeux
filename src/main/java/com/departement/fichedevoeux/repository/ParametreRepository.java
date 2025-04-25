package com.departement.fichedevoeux.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.departement.fichedevoeux.model.Parametre;

public interface ParametreRepository extends JpaRepository<Parametre, Long> {
    Parametre findTopByOrderByIdDesc(); // pour obtenir la dernière deadline
}
