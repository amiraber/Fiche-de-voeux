package DTO;

public class MessageRequestDTO {
    private Long senderId;
    private Long conversationId;
    private String content;

    public Long getSenderId() {
        return senderId;
    }
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getConversationId() {
        return conversationId;
    }
    public void setConversationId(long l) {
        this.conversationId = (long) l;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
