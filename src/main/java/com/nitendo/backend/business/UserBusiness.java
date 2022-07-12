package com.nitendo.backend.business;

import com.nitendo.backend.entity.User;
import com.nitendo.backend.exception.BaseException;
import com.nitendo.backend.exception.UserException;
import com.nitendo.backend.mapper.UserMapper;
import com.nitendo.backend.model.*;
import com.nitendo.backend.service.TokenService;
import com.nitendo.backend.service.UserService;
import com.nitendo.backend.util.SecurityUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@Log4j2
public class UserBusiness {
    private final UserService userService;
    private final UserMapper userMapper;
    private final TokenService tokenService;
    private final EmailBusiness emailBusiness;

    public UserBusiness(UserService userService, UserMapper userMapper, TokenService tokenService, EmailBusiness emailBusiness) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
        this.emailBusiness = emailBusiness;
    }

    // Method 1 Get error
//    public String register(MRegisterRequest request) throws IOException {
//        if (request == null) {
//            throw new IOException("null.request");
//        }
//
//        // Validate email
//        if (Objects.isNull(request.getEmail()) ) {
//            throw new IOException("null.email");
//        }
//
//        // Validate ...
//        return "";
//    }
    // Method 2 Get error

    public MRegisterResponse register(MRegisterRequest request) throws BaseException {
        String token = SecurityUtil.generateToken();
        User user = userService.create(request.getEmail(), request.getPassword(), request.getName(), token, nextXMinute(30));

        sendEmail(user);

        // Mapper class
        return userMapper.toRegisterResponse(user);
    }

    public MActivateResponse activate(MActivateRequest request) throws BaseException {
        String token = request.getToken();
        if (StringUtil.isNullOrEmpty(token)) {
            throw UserException.activateNoToken();
        }

        Optional<User> opt = userService.findByToken(token);
        if (opt.isEmpty()) {
            throw UserException.activateFail();
        }

        User user = opt.get();
        if ( user.isActivated() ) {
            throw UserException.activateAlready();
        }

        Date now = new Date();
        Date expireDate = user.getTokenExpire();
        if (now.after(expireDate)) {    // Current date มากกว่าวันหมดอายุ
            throw UserException.activateTokenExpire();
        }

        user.setActivated(true);
        userService.update(user);

        MActivateResponse response = new MActivateResponse();
        response.setSuccess(true);
        return response;
    }

    public void resendActivationEmail(MResendActivationEmailRequest request) throws BaseException {
        String email = request.getEmail();
        if (StringUtil.isNullOrEmpty(email)) {
            throw UserException.resendActivationEmailNoEmail();
        }
        Optional<User> opt = userService.findByEmail(email);
        if (opt.isEmpty()) {
            throw UserException.resendActivationEmailNotFound();
        }

        User user = opt.get();
        if ( user.isActivated() ) {
            throw UserException.activateAlready();
        }
        user.setToken(SecurityUtil.generateToken());
        user.setTokenExpire(nextXMinute(30));
        user = userService.update(user);

        sendEmail(user);
    }

    private Date nextXMinute(int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    private void sendEmail(User user) {
        //TODO: generate token
        String token = user.getToken();

        try {
            emailBusiness.sendActivateUserEmail(user.getEmail(), user.getName(), token);
        } catch (BaseException e) {
            e.printStackTrace();
        }
    }

    public MLoginResponse login(MLoginRequest request) throws UserException {
        // validate request
        // verify database
        Optional<User> opt = userService.findByEmail(request.getEmail());
        if ( opt.isEmpty() ) {
            throw UserException.loginFailEmailNotFound();    // throw login fail, email not found
        }
        // verify password
        User user = opt.get();
        if ( !userService.matchPassword(request.getPassword(), user.getPassword())) {
            throw UserException.loginFailPasswordIncorrect();   // throw login fail, password incorrect
        }

        // verify activate status
        if ( !user.isActivated() ) {
            throw UserException.loginFailUserUnactivated();
        }

        MLoginResponse response = new MLoginResponse();
        response.setToken(tokenService.tokenize(user));
        return response;
    }

        public String refreshToken() throws BaseException {
            Optional<String> opt = SecurityUtil.getCurrentUserId();
            if ( opt.isEmpty() ) {
                throw UserException.unauthorized();
            }

            String userId = opt.get();
            Optional<User> optUser = userService.findById(userId);
            if ( optUser.isEmpty() ) {
                throw UserException.notFound();
            }

            User user = optUser.get();
            return tokenService.tokenize(user);
        }
}
