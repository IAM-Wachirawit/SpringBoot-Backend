package com.nitendo.backend.business;

import com.nitendo.backend.entity.User;
import com.nitendo.backend.exception.BaseException;
import com.nitendo.backend.exception.UserException;
import com.nitendo.backend.mapper.UserMapper;
import com.nitendo.backend.model.MLoginRequest;
import com.nitendo.backend.model.MRegisterRequest;
import com.nitendo.backend.model.MRegisterResponse;
import com.nitendo.backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserBusiness {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserBusiness(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
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
    
    public String login(MLoginRequest request) throws UserException {
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

        // TODO: generate JWT
        String token = "JWT TO DO";
        return token;
    }

}
