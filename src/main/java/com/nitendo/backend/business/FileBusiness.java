package com.nitendo.backend.business;

import com.nitendo.backend.exception.BaseException;
import com.nitendo.backend.exception.FileException;
import com.nitendo.backend.exception.UserException;
import com.nitendo.backend.model.MRegisterRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class FileBusiness {

    public String uploadProfilePicture(MultipartFile file) throws BaseException {
        // Validate file
        if ( file == null ) {
            throw FileException.fileNull(); // throw error
        }

        // Validate size
        if ( file.getSize() > 1048576 * 2 ) {
            throw  FileException.fileMaxSize();  // throw error
        }

        // Validate check file type null
        String contentType = file.getContentType();
        if ( contentType == null ) {
            throw FileException.upsupportes();  // throw error
        }

        // Validate check file type jpeg, png
        List<String> supportedTypes = Arrays.asList("image/jpeg", "image/png");
        if ( supportedTypes.contains(contentType) ) {
            throw FileException.upsupportes();  // throw error (unsupport)
        }

        // TODO : upload file File Stroage (AWS S3, etc...)
        try {
            byte[] bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
