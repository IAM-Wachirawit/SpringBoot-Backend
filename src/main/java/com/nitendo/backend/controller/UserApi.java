package com.nitendo.backend.controller;

import com.nitendo.backend.exception.BaseException;
import com.nitendo.backend.business.UserBusiness;
import com.nitendo.backend.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserApi {
    // Method 1 = Field Injection
//    @Autowired
//    private TestBusiness business;

    // Method 2 = Constructor Injector
    private final UserBusiness business;

    public UserApi(UserBusiness business) {
        this.business = business;
    }
//----------Method 2 is Performance faster the method 1-------//

    // Return data format String
    @GetMapping("/home")
    public String testString() {
        return "Hello Test";
    }

    // Return data format JSON
    @GetMapping("/test")
    public TestResponse test() {
        TestResponse response = new TestResponse();
        response.setName("Nat");
        response.setFood("KFC");
        return response;
    }
    // Method 1 Get error
//    // Send data format JSON
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody MRegisterRequest request) {
//        String response;
//        try {
//            response = business.register(request);
//            return ResponseEntity.ok(response);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
//        }
//    }

    // Method 2 Get error
    // Send data format JSON
    @PostMapping("/register")
    public ResponseEntity<MRegisterResponse> register(@RequestBody MRegisterRequest request) throws BaseException {
        MRegisterResponse response = business.register(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<MUserProfile> getMyUserProfile() throws BaseException {
        MUserProfile response = business.getMyUserProfile();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<MUserProfile> updateMyUserProfile(@RequestBody MUpdateUserProfileRequest request) throws BaseException {
        MUserProfile response = business.updateMyUserProfile(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<MLoginResponse> login(@RequestBody MLoginRequest request) throws BaseException {
        MLoginResponse response = business.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/activate")
    public ResponseEntity<MActivateResponse> activate(@RequestBody MActivateRequest request) throws BaseException {
        MActivateResponse response = business.activate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-activation-email")
    public ResponseEntity<Void> activate(@RequestBody MResendActivationEmailRequest request) throws BaseException {
       business.resendActivationEmail(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshToken() throws BaseException {
        String response = business.refreshToken();
        return ResponseEntity.ok(response);
    }

    // TODO: To be deleted
    @DeleteMapping("/test-delete-my-account")
    public ResponseEntity<Void> testDeleteMyAccount() throws BaseException {
        business.testDeleteMyAccount();
        return ResponseEntity.ok().build();
    }


}

