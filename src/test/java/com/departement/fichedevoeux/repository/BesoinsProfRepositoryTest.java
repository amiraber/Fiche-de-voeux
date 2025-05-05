package com.departement.fichedevoeux.repository;

import com.departement.fichedevoeux.model.BesoinsProf;
import com.departement.fichedevoeux.model.Professeur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BesoinsProfRepositoryTest {

    @Autowired
    private BesoinsProfRepository besoinsProfRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Test
    public void testFindByProfesseurId() {
        // Créer un professeur
        Professeur prof = new Professeur();
        prof.setNom("Doe");
        prof.setPrenom("John");
        prof.setEmail("john.doe@example.com");
        prof.setMotDePasse("password");
        prof = professeurRepository.save(prof);

        // Créer un besoin lié à ce professeur
        BesoinsProf besoin = new BesoinsProf();
        besoin.setProfesseur(prof);
        besoin.setAnneeScolaire("2024/2025");
        besoin.setEmailPrefere("john.doe@univ.com");
        besoin.setHeuresSuppS1(1);
        besoin.setHeuresSuppS2(1);
        besoin.setNbrHeuresSuppS1(5);
        besoin.setNbrHeuresSuppS2(3);
        besoin.setNbrPfeLicence(2);
        besoin.setNbrPfeMaster(1);
        besoin.setStatut("soumis");

        besoinsProfRepository.save(besoin);

        // Tester la méthode
        List<BesoinsProf> result = besoinsProfRepository.findByProfesseurId(prof.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAnneeScolaire()).isEqualTo("2024/2025");
        assertThat(result.get(0).getEmailPrefere()).isEqualTo("john.doe@univ.com");
    }
}
