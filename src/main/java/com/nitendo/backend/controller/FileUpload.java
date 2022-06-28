package com.nitendo.backend.controller;

import com.nitendo.backend.exception.BaseException;
import com.nitendo.backend.business.FileBusiness;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class FileUpload {

    private final FileBusiness business;

    public FileUpload(FileBusiness business) {
        this.business = business;
    }

    @PostMapping
    public ResponseEntity<String> uploadProfilePicture(@RequestPart MultipartFile file) throws BaseException {
        String response = business.uploadProfilePicture(file);
        return ResponseEntity.ok(response);
    }

}

