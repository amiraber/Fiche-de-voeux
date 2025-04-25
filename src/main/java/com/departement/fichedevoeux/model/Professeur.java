package com.departement.fichedevoeux.model;

import jakarta.persistence.*;

@Entity
@Table(name = "professeur")

public class Professeur {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prof")
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String motDePasse;
    
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @ManyToOne
    @JoinColumn(name = "id_departement", nullable = false)
    private Departement departement;

	@Column(name = "is_chef", columnDefinition = "INTEGER DEFAULT 0")
    private boolean isChef;
	
	private int numBureau;
	
	//  Constructeurs :
	
	public Professeur() {}
	
	public Professeur(Long id, String nom, String prenom, String email, String motDePasse, Grade grade,
			Departement departement, boolean isChef, int numBureau) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.motDePasse = motDePasse;
		this.grade = grade;
		this.departement = departement;
		this.isChef = isChef;
		this.numBureau = numBureau;
	}

	// getters et setters :
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public Departement getDepartement() {
		return departement;
	}

	public void setDepartement(Departement departement) {
		this.departement = departement;
	}

	public boolean isChef() {
		return isChef;
	}

	public void setChef(boolean isChef) {
		this.isChef = isChef;
	}

	public int getNumBureau() {
		return numBureau;
	}

	public void setNumBureau(int numBureau) {
		this.numBureau = numBureau;
	}
}
