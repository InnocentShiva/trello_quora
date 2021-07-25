package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity){
        entityManager.persist(userEntity);
        return userEntity;
    }

    public UserEntity getTheUserByUserUUId(final String userId){
        try{
            return entityManager.createNamedQuery("userByUserUUID", UserEntity.class).setParameter("uuid", userId).getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    public UserEntity getUserByUsername(final String username){
        try{
            return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", username).getSingleResult();
        }catch(NoResultException nrfe){
            return null;
        }
    }

    public UserEntity getUserByEmail(final String email){
        try{
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        }catch(NoResultException nrfe){
            return null;
        }
    }

    public UserAuthEntity getUserAuthEntityByUserId(final UserEntity user){
        try{
            return entityManager.createNamedQuery("userEntryByUserId", UserAuthEntity.class).setParameter("user" , user).setMaxResults(1).getSingleResult();
        }catch(NoResultException nrfe){
            return null;
        }
    }


    public UserAuthEntity getUserAuthEntityByAccessToken(final String accessToken){
        try{
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class).setParameter("accessToken" , accessToken).getSingleResult();
        }catch(NoResultException nrfe){
            return null;
        }
    }


    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthEntity){
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }

    public void updateUserLoginDetailInDb(final UserAuthEntity userAuthEntity){
        entityManager.merge(userAuthEntity);
    }



}
