package com.departement.fichedevoeux.repository;


import com.departement.fichedevoeux.model.Voeux;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoeuxRepository extends JpaRepository<Voeux, Long> {
}