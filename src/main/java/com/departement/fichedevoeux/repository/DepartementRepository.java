package com.departement.fichedevoeux.repository;

import com.departement.fichedevoeux.model.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartementRepository extends JpaRepository<Departement, Long> {
}
