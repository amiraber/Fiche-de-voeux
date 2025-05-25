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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_module", nullable = false)
    private Module module;

    private String semestre;

    private String nature;
    
    private int annee;

    @Column(name = "num_choix")
    private int numChoix;

    public Voeux() {}

    public Voeux(Professeur professeur, Module module, String semestre, String nature,int annee, int numChoix) {
        this.professeur = professeur;
        this.module = module;
        this.semestre = semestre;
        this.nature = nature;
        this.annee = annee;
        this.numChoix = numChoix;
    }

    public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
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

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
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
