package DTO;

public class ProfesseurDTO {
	
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private Long departement;
    private Integer conversationId;
    
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
	public Long getDepartement() {
		return departement;
	}
	public void setDepartement(Long departement) {
		this.departement = departement;
	}
    
	public Integer getConversationId() {
	    return conversationId;
	}
	
	public void setConversationId(Integer conversationId) {
	    this.conversationId = conversationId;
	}
	
	

}
