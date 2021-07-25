package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(method = RequestMethod.POST, path = "/user/signup" , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signUp(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {
        final UserEntity userEntity= new UserEntity();
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setContact_number(signupUserRequest.getContactNumber());
        userEntity.setUsername(signupUserRequest.getUserName());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setDob(signupUserRequest.getDob());
        userEntity.setAbout_me(signupUserRequest.getAboutMe());
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setRole("nonadmin");

        final UserEntity createdUserEntity = userService.signup(userEntity);
        SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status("REGISTERED");
        return new ResponseEntity<SignupUserResponse>(userResponse , HttpStatus.CREATED);


    }

    @RequestMapping(method = RequestMethod.POST, path = "/user/signin" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> login(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {

        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodeText = new String(decode);
        String[] decodedArray = decodeText.split(":");

        UserAuthEntity userAuthEntity = userService.verificationOfUserLogin(decodedArray[0], decodedArray[1]);

        UserEntity userEntity = userAuthEntity.getUser();
        SigninResponse signinResponse = new SigninResponse().id(userEntity.getUuid())
                        .message("SIGNED IN SUCCESSFULLY");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("access-token", userAuthEntity.getAccessToken());

        return new ResponseEntity<SigninResponse>(signinResponse, httpHeaders, HttpStatus.OK);


    }


    @RequestMapping(method = RequestMethod.POST, path = "/user/signout" , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> logout(@RequestHeader("authorization") final String authorization) throws SignOutRestrictedException {
//        String accessToken = authorization.split("access: ")[1];

        String accessToken = authorization;



        UserEntity userEntity = userService.logOutTheUser(accessToken);
        SignoutResponse signoutResponse = new SignoutResponse().id(userEntity.getUuid())
                .message("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(signoutResponse , HttpStatus.ACCEPTED);
    }




}
