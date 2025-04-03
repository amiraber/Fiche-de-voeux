package com.departement.fichedevoeux.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departement")
public class Departement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_departement")
	private Long id;
	
	@Column(name = "nom_departement", nullable = false)
	private String nom;

	@OneToMany(mappedBy = "departement", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Professeur> professeurs = new ArrayList<>();

	// Constructeurs :
	public Departement() {}

	public Departement(String nom) {
	    this.nom = nom;
	}

	// Getters et Setters :
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

	public List<Professeur> getProfesseurs() { 
		return professeurs; 
	}
	public void setProfesseurs(List<Professeur> professeurs) { 
		this.professeurs = professeurs; 
	}

}
