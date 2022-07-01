package com.nitendo.backend.controller;

import com.nitendo.backend.business.ChatBusiness;
import com.nitendo.backend.exception.BaseException;
import com.nitendo.backend.model.ChatMessageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatApi {
    private final ChatBusiness businessusiness;

    public ChatApi(ChatBusiness businessusiness) {
        this.businessusiness = businessusiness;
    }

    @PostMapping("/message")
    public ResponseEntity<Void> post(@RequestBody ChatMessageRequest request) throws BaseException {
        businessusiness.post(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
