package com.nitendo.backend.model;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessage {
    private String from;        // message ของใคร
    private String message;     // message ข้อความ
    private Date created;       // message ถูกส่งเมื่อไร

    public ChatMessage() {
        created = new Date();
    }

}
