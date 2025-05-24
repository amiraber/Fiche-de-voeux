package com.departement.fichedevoeux.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "conversation")

public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conversation")
    private int idConversation;

    @Column(nullable = false)
    private String sujet;

    @Column(name = "date_creation")
    private String dateCreation;

    @ManyToOne
    @JoinColumn(name = "id_initiateur", nullable = false)
    private Professeur initiateur;
    
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<Message> messages;

    // Constructeurs, getters, setters...
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Conversation() {}

    public Conversation(String sujet, String dateCreation, Professeur initiateur) {
        this.sujet = sujet;
        this.dateCreation = dateCreation;
        this.initiateur = initiateur;
    }

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

    public Professeur getInitiateur() {
        return initiateur;
    }

    public void setInitiateur(Professeur initiateur) {
        this.initiateur = initiateur;
    }
}
