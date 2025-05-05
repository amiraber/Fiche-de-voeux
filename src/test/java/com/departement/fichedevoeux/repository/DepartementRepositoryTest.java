package com.departement.fichedevoeux.repository;

import com.departement.fichedevoeux.model.Departement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DepartementRepositoryTest {

    @Autowired
    private DepartementRepository departementRepository;

    @Test
    @DisplayName("Should find departement by exact name")
    void testFindByNomDepartement() {
        Departement departement = new Departement("Informatique");
        departementRepository.save(departement);

        Departement found = departementRepository.findByNomDepartement("Informatique");

        assertThat(found).isNotNull();
        assertThat(found.getNomDepartement()).isEqualTo("Informatique");
    }

    @Test
    @DisplayName("Should find departements by name containing string (case-insensitive)")
    void testFindByNomDepartementContainingIgnoreCase() {
        departementRepository.save(new Departement("Informatique"));
        departementRepository.save(new Departement("Mathematiques"));
        departementRepository.save(new Departement("Physique"));

        List<Departement> results = departementRepository.findByNomDepartementContainingIgnoreCase("mat");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getNomDepartement()).isEqualTo("Mathematiques");
    }

    @Test
    @DisplayName("Should return empty list when no match found")
    void testFindByNomDepartementContainingIgnoreCase_NoMatch() {
        departementRepository.save(new Departement("Biologie"));

        List<Departement> results = departementRepository.findByNomDepartementContainingIgnoreCase("xyz");

        assertThat(results).isEmpty();
    }
}
