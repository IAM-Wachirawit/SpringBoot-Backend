package com.nitendo.backend.business;

import com.nitendo.backend.entity.User;
import com.nitendo.backend.exception.BaseException;
import com.nitendo.backend.exception.UserException;
import com.nitendo.backend.mapper.UserMapper;
import com.nitendo.backend.model.MLoginRequest;
import com.nitendo.backend.model.MLoginResponse;
import com.nitendo.backend.model.MRegisterRequest;
import com.nitendo.backend.model.MRegisterResponse;
import com.nitendo.backend.service.TokenService;
import com.nitendo.backend.service.UserService;
import com.nitendo.backend.util.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserBusiness {
    private final UserService userService;
    private final UserMapper userMapper;
    private final TokenService tokenService;

    public UserBusiness(UserService userService, UserMapper userMapper, TokenService tokenService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
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
        User user = userService.create(request.getEmail(), request.getPassword(), request.getName());
        // Mapper class
        return userMapper.toRegisterResponse(user);
    }
    
    public MLoginResponse login(MLoginRequest request) throws UserException {
        // validate request
        // verify database
        Optional<User> opt = userService.findByEmail(request.getEmail());
        if ( opt.isEmpty() ) {
            throw UserException.loginFailEmailNotFound();    // throw login fail, email not found
        }
        // Check password
        User user = opt.get();
        if ( !userService.matchPassword(request.getPassword(), user.getPassword())) {
            throw UserException.loginFailPasswordIncorrect();   // throw login fail, password incorrect
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
