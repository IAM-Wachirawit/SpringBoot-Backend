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



// Activate
    public static UserException activateNoToken() {
        return new UserException("activate.no.token");
    }

    public static UserException activateAlready() {
        return new UserException("activate.already");
    }

    public static UserException activateFail() {
        return new UserException("activate.fail");
    }

    public static UserException activateTokenExpire() {
        return new UserException("activate.token.expire");
    }

    public static UserException loginFailUserUnactivated() {
        return new UserException("login.fail.unactivated");
    }

    // Resend Activation Email
    public static UserException resendActivationEmailNoEmail() {
        return new UserException("resend.activation.no.email");
    }

    public static UserException resendActivationEmailNotFound() {
        return new UserException("resend.activation.fail");
    }
}
