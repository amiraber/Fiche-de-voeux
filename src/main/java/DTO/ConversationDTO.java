package DTO;
//dto/ConversationDTO.java
public class ConversationDTO {
 private int idConversation;
 private String sujet;
 private String dateCreation;
 private String initiateurNom;
 // Getters & setters
public int getIdConversation() {
	return idConversation;
}
public void setIdConversation(int idConversation) {
	this.idConversation = idConversation;
}
public String getSujet() {
	return sujet;
}
public void setSujet(String sujet) {
	this.sujet = sujet;
}
public String getDateCreation() {
	return dateCreation;
}
public void setDateCreation(String dateCreation) {
	this.dateCreation = dateCreation;
}
public String getInitiateurNom() {
	return initiateurNom;
}
public void setInitiateurNom(String initiateurNom) {
	this.initiateurNom = initiateurNom;
}
 
}
