package com.departement.fichedevoeux.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.departement.fichedevoeux.model.Parametre;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.repository.FicheDeVoeuxRepository;
import com.departement.fichedevoeux.repository.ParametreRepository;
import com.departement.fichedevoeux.repository.ProfesseurRepository;

@Service
public class AdminService {

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private ParametreRepository parametreRepository; // Pour stocker deadline
    
    @Autowired
    private FicheDeVoeuxRepository ficheDeVoeuxRepository; // Pour l’export du fichier Excel


    // Vérifie si un professeur est chef de département
    public boolean isChef(Long profId) {
        Professeur prof = professeurRepository.findById(profId).orElse(null);
        return prof != null && prof.isChef();
    }

    // Mise à jour ou création de la deadline
    public boolean setDeadline(LocalDate nouvelleDate) {
        Parametre param = new Parametre();
        param.setDeadline(nouvelleDate);
        parametreRepository.save(param);
        return true;
    }

    // Récupération de la deadline active
    public LocalDate getDeadline() {
        Parametre dernier = parametreRepository.findTopByOrderByIdDesc();
        return (dernier != null) ? dernier.getDeadline() : null;
    }

    // Simulation de l’export Excel (remplacer par POI plus tard)
    public byte[] exporterExcel() {
        // Ici tu ajouteras la génération réelle avec Apache POI ( la construction du fichier)
       return null ; // remplace null plus tard
    }
}
