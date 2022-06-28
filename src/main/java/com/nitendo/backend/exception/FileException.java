package com.nitendo.backend.exception;

public class FileException extends BaseException {

    public FileException(String code) {
        super("File." + code);
    }

    public static FileException fileNull() {
        return new FileException("null");
    }

    public static FileException fileMaxSize() {
        return new FileException("max.size");
    }

    public static FileException upsupportes() {
        return new FileException("unsupported.file.type");
    }
}
