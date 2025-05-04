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
	private String nomDepartement;

	@OneToMany(mappedBy = "departement", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Professeur> professeurs = new ArrayList<>();

	// Constructeurs :
	public Departement() {}

	public Departement(String nomDepartement) {
	    this.nomDepartement = nomDepartement;
	}

	// Getters et Setters :
	public Long getId() { 
		return id; 
	}
	
	public void setId(Long id) { 
		this.id = id; 
	}

	public String getNomDepartement() { 
		return nomDepartement; 
	}
	
	public void setNomDepartement(String nomDepartement) { 
		this.nomDepartement = nomDepartement;
	}

	public List<Professeur> getProfesseurs() { 
		return professeurs; 
	}
	public void setProfesseurs(List<Professeur> professeurs) { 
		this.professeurs = professeurs; 
	}

}
