package com.departement.fichedevoeux.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.departement.fichedevoeux.model.Parametre;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.model.Voeux;
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
    public byte[] exporterExcel(){
    	try {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Voeux");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Professeur");
        header.createCell(1).setCellValue("Module");
        header.createCell(2).setCellValue("Semestre");
        header.createCell(3).setCellValue("Nature");

        List<Voeux> voeuxList = ficheDeVoeuxRepository.findAll();
        int rowIndex = 1;

        for (Voeux v : voeuxList) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(v.getProfesseur().getNom());
            row.createCell(1).setCellValue(v.getModule().getNom());
            row.createCell(2).setCellValue(v.getSemestre());
            row.createCell(3).setCellValue(v.getNature());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
 
			workbook.write(out);
			return out.toByteArray();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }
}
