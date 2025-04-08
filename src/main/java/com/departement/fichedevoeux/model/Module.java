package com.departement.fichedevoeux.model;

import jakarta.persistence.*;

@Entity
@Table(name = "module")

public class Module {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_module")
    private int idModule;

    @Column(nullable = false)
    private String pallier;

    private String specialite;

    @Column(nullable = false)
    private String semestre;

    @Column(nullable = false)
    private String nom;

    public Module() {}

    public Module(String pallier, String specialite, String semestre, String nom) {
        this.pallier = pallier;
        this.specialite = specialite;
        this.semestre = semestre;
        this.nom = nom;
    }

    public int getIdModule() {
        return idModule;
    }

    public void setIdModule(int idModule) {
        this.idModule = idModule;
    }

    public String getPallier() {
        return pallier;
    }

    public void setPallier(String pallier) {
        this.pallier = pallier;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
