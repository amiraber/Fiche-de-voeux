package DTO;

import com.departement.fichedevoeux.model.Departement;

public class RegisterRequestDTO {
  
	private String email;
    private String password;
    private String nom;
    private String prenom;
    
	
	public RegisterRequestDTO(String email, String password, String nom, String prenom, String departement) {
		super();
		this.email = email;
		this.password = password;
		this.nom = nom;
		this.prenom = prenom;
		this.departement = departement;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	private  String departement;
    
	public RegisterRequestDTO() {
		super();
	}
	public String getDepartement() {
		return departement;
	}
	public void setDepartement(String departement) {
		this.departement = departement;
	}
    
    
	
}
