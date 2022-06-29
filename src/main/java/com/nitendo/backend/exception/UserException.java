package com.nitendo.backend.exception;

public class UserException extends BaseException {
// Register
    public UserException(String code) {
        super("User." + code);
    }

    public static UserException unauthorized() {
        return new UserException("unauthorized");
    }

    public static UserException notFound() {
        return new UserException("not.found");
    }

    // User.register.request.null
    public static UserException requestNull() {
        return new UserException("register.request.null");
    }

    // User.register.email.null
    public static UserException emailNull() {
        return new UserException("register.email.null");
    }

// Create
    public static UserException createEmailNull() {
        return new UserException("create.email.null");
    }
    public static UserException createEmailDuplicated() {
        return new UserException("create.email.duplicated");
    }
    public static UserException createPasswordNull() {
        return new UserException("create.password.null");
    }
    public static UserException createNameNull() {
        return new UserException("create.name.null");
    }

// Login
    public static UserException loginFailEmailNotFound() {
        return new UserException("login.fail");
    }
    public static UserException loginFailPasswordIncorrect() {
        return new UserException("login.fail");
    }
}
