package com.nitendo.backend.business;

import com.nitendo.backend.exception.BaseException;
import com.nitendo.backend.exception.ChatException;
import com.nitendo.backend.model.ChatMessageRequest;
import com.nitendo.backend.model.ChatMessage;
import com.nitendo.backend.util.SecurityUtil;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatBusiness {
    // Spring มี class ไว้ให้ใช้สำหรับการส่งข้อความไปยังสถานที่หนึ่ง ผ่านไปยัง protocol
    private final SimpMessagingTemplate template;

    public ChatBusiness(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void post(ChatMessageRequest request) throws BaseException {
        Optional<String> opt = SecurityUtil.getCurrentUserId();
        // Check ต้องมี user login
        if ( opt.isEmpty() ) {
            throw ChatException.accessDenied();
        }

        // TODO: Validate message

        final String destination = "/topic/chat"; // คุยผ่าน channel ชื่อ chat

        ChatMessage payload = new ChatMessage();
        payload.setFrom(opt.get());
        payload.setMessage(request.getMessage());

        template.convertAndSend(destination, payload); // Function การส่งข้อความกลับไปให้ front end จาก Object SimpMessagingTemplate
    }

}
