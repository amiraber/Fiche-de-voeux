package DTO;

public class ConversationRequestDTO {

    private Long initiateurId;
    private String sujet;

    public Long getInitiateurId() {
        return initiateurId;
    }

    public void setInitiateurId(Long initiateurId) {
        this.initiateurId = initiateurId;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }
}
