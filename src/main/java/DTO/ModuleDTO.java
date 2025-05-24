package DTO;

public class ModuleDTO {
    private String niveau;
    private String specialite;
    private String nom;
    private String pallier;
    private String semestre;

    public String getPallier() {
        return pallier;
    }
    public void setPallier(String pallier) {
        this.pallier = pallier;
    }

    public String getSemestre() {
        return semestre;
    }
    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }


    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}
