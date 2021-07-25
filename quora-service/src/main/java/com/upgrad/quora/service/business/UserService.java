package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {


        verification(userEntity.getUsername() , userEntity.getEmail());


        String[] encryptText = cryptographyProvider.encrypt(userEntity.getPassword());

        userEntity.setSalt(encryptText[0]);
        userEntity.setPassword(encryptText[1]);

        return userDao.createUser(userEntity);

    }

    public void verification(String username , String email) throws SignUpRestrictedException {
        UserEntity userEntity1 = userDao.getUserByUsername(username);



        UserEntity userEntity2 = userDao.getUserByEmail(email);

        if(userEntity1 != null && userEntity2 != null){
            throw new SignUpRestrictedException("SGR-003" , "This user has already been registered, try with different emailId and Username");
        }


        if(userEntity1 != null){
            throw new SignUpRestrictedException("SGR-001" , "Try any other Username, this Username has already been taken");
        }

        if(userEntity2 != null){
            throw new SignUpRestrictedException("SGR-002" , "This user has already been registered, try with any other emailId");
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity verificationOfUserLogin(String username , String password) throws AuthenticationFailedException {

        UserEntity userEntity = userDao.getUserByUsername(username);

        if(userEntity == null) {
            throw new AuthenticationFailedException( "ATH-001" , "This username does not exist");
        }

        final String encryptedPassword = cryptographyProvider.encrypt(password, userEntity.getSalt());

        if(encryptedPassword.equals(userEntity.getPassword())){
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

            UserAuthEntity userAuthEntity = new UserAuthEntity();
            userAuthEntity.setUser(userEntity);
            userAuthEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
            userAuthEntity.setLoginAt(now);
            userAuthEntity.setExpiresAt(expiresAt);
            userAuthEntity.setUuid(userEntity.getUuid());
            if(userDao.getTheUserByUserUUId(userAuthEntity.getUuid())==null) {
                System.out.println("This is working");
                userDao.createAuthToken(userAuthEntity);
            }else{
            updateLoginInfo(userAuthEntity);}
            return userAuthEntity;

        }else{
            throw new AuthenticationFailedException("ATH-002" , "Password failed");
        }

//        UserEntity userEntity2 = userDao.getUserByPassword(password);

    }

    public void updateLoginInfo(final UserAuthEntity userAuthEntity){

            userDao.updateUserLoginDetailInDb(userAuthEntity);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity logOutTheUser(final String accessToken) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthEntityByAccessToken(accessToken);
        if(userAuthEntity != null){
            final ZonedDateTime now = ZonedDateTime.now();
            userAuthEntity.setLogoutAt(now);
            userDao.updateUserLoginDetailInDb(userAuthEntity);
            UserEntity userEntity = userAuthEntity.getUser();
            return userEntity;
        }else{
            throw new SignOutRestrictedException("SGR-001","User is not Signed in");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity getTheUser(final String userUUID) throws UserNotFoundException {
        UserEntity userEntity = userDao.getTheUserByUserUUId(userUUID);
        if(userEntity != null){
            return userEntity;
        }else{
            throw new UserNotFoundException("USR-001" , "User with entered uuid does not exist");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity getTheUserLoginDetailUsingAuthToken(String access_token) throws AuthorizationFailedException, ParseException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthEntityByAccessToken(access_token);
        if(userAuthEntity != null ){
            if(userAuthEntity.getLogoutAt()!=null){
                SimpleDateFormat logout_date_time_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date logoutTime = logout_date_time_format.parse(userAuthEntity.getLogoutAt().toString().replace("T"," "));
                Date loginTime = logout_date_time_format.parse(userAuthEntity.getLoginAt().toString().replace("T"," "));
                if(logoutTime.before(loginTime)){
                    return userAuthEntity;
                }
                else {
                    throw new AuthorizationFailedException("ATHR-002" , "User is signed out.Sign in first to get user details");
                }
            }else{
                return userAuthEntity;
            }
        }else{
            throw new AuthorizationFailedException("ATHR-001" , "User has not signed in");
        }
    }


}
