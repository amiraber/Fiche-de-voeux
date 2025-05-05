package com.departement.fichedevoeux.repository;

import com.departement.fichedevoeux.model.Professeur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest
public class ProfesseurRepositoryTest {

    @Autowired
    private ProfesseurRepository professeurRepository;

    private Professeur professeur;

    @BeforeEach
    void setUp() {
        professeur = new Professeur();
        professeur.setNom("John");
        professeur.setPrenom("Doe");
        professeur.setEmail("john.doe@example.com");
        professeur.setMotDePasse("password123");
        // Assuming Grade and Departement are also set properly.
        professeurRepository.save(professeur);
    }

    @Test
    void testFindByEmail() {
        Professeur foundProfesseur = professeurRepository.findByEmail("john.doe@example.com");
        assertThat(foundProfesseur).isNotNull();
        assertThat(foundProfesseur.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void testFindByNom() {
        List<Professeur> foundProfesseurs = professeurRepository.findByNom("John");
        assertThat(foundProfesseurs).isNotEmpty();
        assertThat(foundProfesseurs.get(0).getNom()).isEqualTo("John");
    }

    @Test
    void testFindByPrenom() {
        List<Professeur> foundProfesseurs = professeurRepository.findByPrenom("Doe");
        assertThat(foundProfesseurs).isNotEmpty();
        assertThat(foundProfesseurs.get(0).getPrenom()).isEqualTo("Doe");
    }

    @Test
    void testFindByIsChefTrue() {
        professeur.setChef(true);
        professeurRepository.save(professeur);
        List<Professeur> chefs = professeurRepository.findByIsChefTrue();
        assertThat(chefs).isNotEmpty();
        assertThat(chefs.get(0).isChef()).isTrue();
    }
}
