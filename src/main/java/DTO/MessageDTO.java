package DTO;
//dto/MessageDTO.java


public class MessageDTO {
 private String contenu;
 private Long senderId;
 private boolean lu;
 private String typeExpediteur;
 // Getters & setters
public String getContenu() {
	return contenu;
}
public void setContenu(String contenu) {
	this.contenu = contenu;
}
public Long getSenderId() {
	return senderId;
}
public void setSenderId(Long senderId) {
	this.senderId = senderId;
}
public boolean isLu() {
	return lu;
}
public void setLu(boolean lu) {
	this.lu = lu;
}

public String getTypeExpediteur() {
    return typeExpediteur;
}
public void setTypeExpediteur(String typeExpediteur) {
    this.typeExpediteur = typeExpediteur;
}
}


