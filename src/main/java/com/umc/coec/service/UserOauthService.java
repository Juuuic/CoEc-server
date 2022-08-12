package com.umc.coec.service;

import com.umc.coec.config.jwt.JwtModel;
import com.umc.coec.domain.user.UserOauth;
import com.umc.coec.domain.user.UserOauthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class UserOauthService {

      private final UserOauthRepository userOauthRepository;

      @Transactional
      public void deleteUserOauth(String email){
            userOauthRepository.deleteByEmail(email);
      }

      public void insertUserOauth(String email, JwtModel jwtModel){
            UserOauth userOauth = UserOauth.builder().refreshToken(jwtModel.getRefreshToken()).email(email).build();
            userOauthRepository.save(userOauth);
      }
      public UserOauth findUserOauthByEmail(String email){
            return userOauthRepository.findByEmail(email);
      }


}
