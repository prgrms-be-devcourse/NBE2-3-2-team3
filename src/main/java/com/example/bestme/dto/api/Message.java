package com.example.bestme.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

    @JsonProperty("role")
    private String role;

    @JsonProperty("content")
    private String content;

    // 기본 생성자
    public Message() {}

    // 모든 필드를 포함한 생성자
    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    // Getter와 Setter
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

