package com.departement.fichedevoeux.repository;
import com.departement.fichedevoeux.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository <Module, Long> {
	
}
