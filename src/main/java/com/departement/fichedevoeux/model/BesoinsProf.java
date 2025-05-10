package com.departement.fichedevoeux.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "besoins_prof")

public class BesoinsProf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_besoins")
    private Long idBesoins;

    @ManyToOne
    @JoinColumn(name = "id_prof", nullable = false)
    private Professeur professeur;

    @Column(name = "annee_scolaire")
    private String anneeScolaire;

    @Column(name = "heures_supp_s1")
    private Integer heuresSuppS1;

    @Column(name = "heures_supp_s2")
    private Integer heuresSuppS2;

    @Column(name = "nbr_heures_supp_s1")
    private Integer nbrHeuresSuppS1;

    @Column(name = "nbr_heures_supp_s2")
    private Integer nbrHeuresSuppS2;

    @Min(value = 1, message = "Le nombre de PFE Licence doit etre au moins 1")
    @Column(name = "nbr_pfe_licence")
    private Integer nbrPfeLicence;

    @Min(value = 1, message = "Le nombre de PFE Master doit etre au moins 1")
    @Column(name = "nbr_pfe_master")
    private Integer nbrPfeMaster;

    @Column(name = "statut")
    private String statut;

    public BesoinsProf() {}

    // Getters et setters

    public BesoinsProf(Long idBesoins, Professeur professeur, String anneeScolaire,
			Integer heuresSuppS1, Integer heuresSuppS2, Integer nbrHeuresSuppS1, Integer nbrHeuresSuppS2,
			Integer nbrPfeLicence, Integer nbrPfeMaster, String statut) {
		super();
		this.idBesoins = idBesoins;
		this.professeur = professeur;
		this.anneeScolaire = anneeScolaire;
		this.heuresSuppS1 = heuresSuppS1;
		this.heuresSuppS2 = heuresSuppS2;
		this.nbrHeuresSuppS1 = nbrHeuresSuppS1;
		this.nbrHeuresSuppS2 = nbrHeuresSuppS2;
		this.nbrPfeLicence = nbrPfeLicence;
		this.nbrPfeMaster = nbrPfeMaster;
		this.statut = statut;
	}

	public Long getIdBesoins() {
        return idBesoins;
    }

    public void setIdBesoins(Long idBesoins) {
        this.idBesoins = idBesoins;
    }

    public Professeur getProfesseur() {
        return professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }

    public String getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public Integer getHeuresSuppS1() {
        return heuresSuppS1;
    }

    public void setHeuresSuppS1(Integer heuresSuppS1) {
        this.heuresSuppS1 = heuresSuppS1;
    }

    public Integer getHeuresSuppS2() {
        return heuresSuppS2;
    }

    public void setHeuresSuppS2(Integer heuresSuppS2) {
        this.heuresSuppS2 = heuresSuppS2;
    }

    public Integer getNbrHeuresSuppS1() {
        return nbrHeuresSuppS1;
    }

    public void setNbrHeuresSuppS1(Integer nbrHeuresSuppS1) {
        this.nbrHeuresSuppS1 = nbrHeuresSuppS1;
    }

    public Integer getNbrHeuresSuppS2() {
        return nbrHeuresSuppS2;
    }

    public void setNbrHeuresSuppS2(Integer nbrHeuresSuppS2) {
        this.nbrHeuresSuppS2 = nbrHeuresSuppS2;
    }

    public Integer getNbrPfeLicence() {
        return nbrPfeLicence;
    }

    public void setNbrPfeLicence(Integer nbrPfeLicence) {
        this.nbrPfeLicence = nbrPfeLicence;
    }

    public Integer getNbrPfeMaster() {
        return nbrPfeMaster;
    }

    public void setNbrPfeMaster(Integer nbrPfeMaster) {
        this.nbrPfeMaster = nbrPfeMaster;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
