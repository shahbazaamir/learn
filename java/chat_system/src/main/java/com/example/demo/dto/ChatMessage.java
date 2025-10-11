package com.example.demo.dto;

public class ChatMessage {
    private String from;
    private String to;
    private String content;

    // getters & setters
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    @Override
    public String toString() {
        return """
                from : %s
                to : %s
                content : %s
                """.formatted(from,to,content);
    }
}
