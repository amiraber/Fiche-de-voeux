package com.departement.fichedevoeux.model;

import jakarta.persistence.*;

@Entity
@Table(name = "message")

public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_message")
    private int idMessage;

    @ManyToOne
    @JoinColumn(name = "id_conversation", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "id_auteur", nullable = false)
    private Professeur auteur;

    @Column(name = "date_envoi")
    private String dateEnvoi;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;

    @Column(name = "est_lu")
    private boolean estLu;

    public Message() {}

    public Message(Conversation conversation, Professeur auteur, String dateEnvoi, String contenu, boolean estLu) {
        this.conversation = conversation;
        this.auteur = auteur;
        this.dateEnvoi = dateEnvoi;
        this.contenu = contenu;
        this.estLu = estLu;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public Professeur getAuteur() {
        return auteur;
    }

    public void setAuteur(Professeur auteur) {
        this.auteur = auteur;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public boolean isEstLu() {
        return estLu;
    }

    public void setEstLu(boolean estLu) {
        this.estLu = estLu;
    }
}
