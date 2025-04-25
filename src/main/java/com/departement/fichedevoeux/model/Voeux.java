package com.departement.fichedevoeux.model;

import jakarta.persistence.*;

@Entity
@Table(name = "voeux")

public class Voeux {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voeux")
    private Long idVoeux;

    @ManyToOne
    @JoinColumn(name = "id_prof", nullable = false)
    private Professeur professeur;

    @ManyToOne
    @JoinColumn(name = "id_module", nullable = false)
    private Module module;

    private int semestre;

    private String nature;

    @Column(name = "num_choix")
    private int numChoix;

    public Voeux() {}

    public Voeux(Professeur professeur, Module module, int semestre, String nature, int numChoix) {
        this.professeur = professeur;
        this.module = module;
        this.semestre = semestre;
        this.nature = nature;
        this.numChoix = numChoix;
    }

    public Long getIdVoeux() {
        return idVoeux;
    }

    public void setIdVoeux(Long idVoeux) {
        this.idVoeux = idVoeux;
    }

    public Professeur getProfesseur() {
        return professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public int getNumChoix() {
        return numChoix;
    }

    public void setNumChoix(int numChoix) {
        this.numChoix = numChoix;
    }
}
