package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping
public class CommonController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET , path="/userprofile/{userId}" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getProfile(@PathVariable("userId") final String userId, @RequestHeader("authorization") final String accessToken) throws UserNotFoundException, AuthorizationFailedException, ParseException {
        final UserEntity userEntity = userService.getTheUser(userId) ;
        final UserAuthEntity userAuthEntity = userService.getTheUserLoginDetailUsingAuthToken(accessToken);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().userName(userEntity.getUsername()).
                firstName(userEntity.getFirstName()).lastName(userEntity.getLastName()).
                aboutMe(userEntity.getAbout_me()).dob(userEntity.getDob()).
                contactNumber(userEntity.getContact_number()).emailAddress(userEntity.getEmail()).
                country(userEntity.getCountry());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);

    }





}
