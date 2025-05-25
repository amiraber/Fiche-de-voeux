package com.departement.fichedevoeux.service;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.departement.fichedevoeux.model.BesoinsProf;
import com.departement.fichedevoeux.model.Parametre;
import com.departement.fichedevoeux.model.Professeur;
import com.departement.fichedevoeux.model.Voeux;
import com.departement.fichedevoeux.repository.BesoinsProfRepository;
import com.departement.fichedevoeux.repository.FicheDeVoeuxRepository;
import com.departement.fichedevoeux.repository.ParametreRepository;
import com.departement.fichedevoeux.repository.ProfesseurRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AdminService {
	
	private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private ParametreRepository parametreRepository; // Pour stocker deadline
    
    @Autowired
    private FicheDeVoeuxRepository ficheDeVoeuxRepository; // Pour l’export du fichier Excel
    
    @Autowired
    private BesoinsProfRepository besoinsProfRepository;


    // Vérifie si un professeur est chef de département
    public boolean isChef(Long profId) {
    	
    	
    	
        Professeur prof = professeurRepository.findById(profId).orElse(null);
        return prof != null && prof.isChef();
    }

    // Mise à jour ou création de la deadline
    public boolean setDeadline(LocalDate nouvelleDate) {
    	
    	log.info("✅ set deadline method acceessed succesfully");
    	
        Parametre param = new Parametre();
        param.setDeadline(nouvelleDate);
        parametreRepository.save(param);
        return true;
    }

    // Récupération de la deadline active
    public LocalDate getDeadline() {
    	
    	log.info("✅ get deadline method acceessed succesfully");
    	
        Parametre dernier = parametreRepository.findTopByOrderByIdDesc();
        return (dernier != null) ? dernier.getDeadline() : null;
    }

 // Simulation de l’export Excel S1 

    public byte[] exporterVoeuxSemestre1(Long profId) {

        return exporterVoeuxPourSemestre(profId, "1", "voeux_semestre1.xlsx");

    }



    // Simulation de l’export Excel S2

    public byte[] exporterVoeuxSemestre2(Long profId) {

        return exporterVoeuxPourSemestre(profId, "2", "voeux_semestre2.xlsx");

    }



    private byte[] exporterVoeuxPourSemestre(Long profId, String semestre, String sheetName) {



        Professeur chef = professeurRepository.findById(profId).orElse(null);

        if (chef == null/* || !chef.isChef()*/) return null;



        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            XSSFSheet sheet = workbook.createSheet(sheetName);



            // Styles pour en-tête uniquement

            CellStyle styleProf = createHeaderStyle(workbook, IndexedColors.LIGHT_BLUE);

            CellStyle styleChoix1 = createHeaderStyle(workbook, IndexedColors.LIGHT_YELLOW);

            CellStyle styleChoix2 = createHeaderStyle(workbook, IndexedColors.LIGHT_GREEN);

            CellStyle styleChoix3 = createHeaderStyle(workbook, IndexedColors.CORAL);

            CellStyle styleBesoins = createHeaderStyle(workbook, IndexedColors.LAVENDER);



            // Création de l'en-tête

            Row header = sheet.createRow(0);

            int col = 0;



            Cell hNom = header.createCell(col++);

            hNom.setCellValue("Nom");

            hNom.setCellStyle(styleProf);



            Cell hPrenom = header.createCell(col++);

            hPrenom.setCellValue("Prénom");

            hPrenom.setCellStyle(styleProf);



            Cell hEmail = header.createCell(col++);

            hEmail.setCellValue("Email");

            hEmail.setCellStyle(styleProf);



            Cell hBureau = header.createCell(col++);

            hBureau.setCellValue("Num Bureau");

            hBureau.setCellStyle(styleProf);



            CellStyle[] choixStyles = {styleChoix1, styleChoix2, styleChoix3};



            for (int i = 0; i < 3; i++) {

                String suffix = " " + (i + 1);

                Cell m = header.createCell(col++);

                m.setCellValue("Module" + suffix);

                m.setCellStyle(choixStyles[i]);



                Cell t = header.createCell(col++);

                t.setCellValue("Type");

                t.setCellStyle(choixStyles[i]);



                Cell p = header.createCell(col++);

                p.setCellValue("Pallier");

                p.setCellStyle(choixStyles[i]);



                Cell s = header.createCell(col++);

                s.setCellValue("Spécialité");

                s.setCellStyle(choixStyles[i]);

            }



            Cell hSupp = header.createCell(col++);

            hSupp.setCellValue("Heures supp ?");

            hSupp.setCellStyle(styleBesoins);



            Cell hNbr = header.createCell(col++);

            hNbr.setCellValue("Heures supp.");

            hNbr.setCellStyle(styleBesoins);



            Cell hPfeL = header.createCell(col++);

            hPfeL.setCellValue("PFE Licence");

            hPfeL.setCellStyle(styleBesoins);



            Cell hPfeM = header.createCell(col++);

            hPfeM.setCellValue("PFE Master");

            hPfeM.setCellStyle(styleBesoins);



            // Remplissage des lignes des professeurs

            List<Professeur> profs = professeurRepository.findByDepartementId(chef.getDepartement().getId());

            int rowIndex = 1;



            for (Professeur p : profs) {

                Row row = sheet.createRow(rowIndex++);

                int c = 0;



                row.createCell(c++).setCellValue(p.getNom());

                row.createCell(c++).setCellValue(p.getPrenom());

                row.createCell(c++).setCellValue(p.getEmail());



                Integer numB = p.getNumBureau();

                row.createCell(c++).setCellValue(numB != null ? numB.toString() : "Null");



                List<Voeux> voeux = ficheDeVoeuxRepository.findWithModuleByProfesseurId(p.getId())
                	    .stream()
                	    .filter(v -> v.getSemestre().equals("S" + semestre)) // car semestre = "1" ou "2"
                	    .sorted(Comparator.comparing(Voeux::getNumChoix))
                	    .toList();




                for (int i = 0; i < 3; i++) {

                    if (i < voeux.size()) {

                        Voeux v = voeux.get(i);

                        row.createCell(c++).setCellValue(v.getModule().getNom());

                        row.createCell(c++).setCellValue(v.getNature());

                        row.createCell(c++).setCellValue(v.getModule().getPallier());

                        row.createCell(c++).setCellValue(v.getModule().getSpecialite());

                    } else {

                        row.createCell(c++).setCellValue("");

                        row.createCell(c++).setCellValue("");

                        row.createCell(c++).setCellValue("");

                        row.createCell(c++).setCellValue("");

                    }

                }



                BesoinsProf besoins = besoinsProfRepository.findByProfesseurId(p.getId())

                        .stream().findFirst().orElse(null);



                if (besoins != null) {

                    if ("1".equals(semestre)) {

                        row.createCell(c++).setCellValue(besoins.getHeuresSuppS1() != null ? "Oui" : "Non");

                        row.createCell(c++).setCellValue(besoins.getNbrHeuresSuppS1() != null ? besoins.getNbrHeuresSuppS1() : 0);

                        row.createCell(c++).setCellValue(besoins.getNbrPfeLicence() != null ? besoins.getNbrPfeLicence() : 1);

                        row.createCell(c++).setCellValue(besoins.getNbrPfeMaster() != null ? besoins.getNbrPfeMaster() : 1);

                    } else {

                        row.createCell(c++).setCellValue(besoins.getHeuresSuppS2() != null ? "Oui" : "Non");

                        row.createCell(c++).setCellValue(besoins.getNbrHeuresSuppS2() != null ? besoins.getNbrHeuresSuppS2() : 0);

                        row.createCell(c++).setCellValue(besoins.getNbrPfeLicence() != null ? besoins.getNbrPfeLicence() : 1);

                        row.createCell(c++).setCellValue(besoins.getNbrPfeMaster() != null ? besoins.getNbrPfeMaster() : 1);

                    }

                } else {

                    row.createCell(c++).setCellValue("");

                    row.createCell(c++).setCellValue("");

                    row.createCell(c++).setCellValue("");

                    row.createCell(c++).setCellValue("");

                }

            }



            for (int i = 0; i <= col; i++) {

                sheet.autoSizeColumn(i);

            }



            ByteArrayOutputStream out = new ByteArrayOutputStream();

            workbook.write(out);

            return out.toByteArray();



        } catch (IOException e) {

            return null;

        }

    }



    // Méthode utilitaire pour créer un style d’en-tête coloré

    private CellStyle createHeaderStyle(XSSFWorkbook workbook, IndexedColors color) {

        CellStyle style = workbook.createCellStyle();

        style.setFillForegroundColor(color.getIndex());

        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;

    }
}
