package com.umc.coec.service;

import com.umc.coec.domain.user.User;
import com.umc.coec.domain.user.UserRepository;
import com.umc.coec.dto.auth.EmailDupCheckDto;
import com.umc.coec.dto.auth.JoinDto;
import com.umc.coec.dto.auth.NicknameDupCheckDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

      private final Logger logger= LoggerFactory.getLogger(getClass());
      private final UserRepository userRepository;
      private final PasswordEncoder bcryptPasswordEncoder;

      private boolean isPasswordEqual(JoinDto joinDto){
            logger.info("비밀번호 일치 로직");
            return joinDto.getPassword().equals(joinDto.getPassword2());
      }

      public boolean join(JoinDto joinDto){
            if(! isPasswordEqual(joinDto)) return false; //TODO: custom exception 으로 메세지까지 던져주기
            logger.info("비밀번호 일치 로직 성공");

            String rawPassword=joinDto.getPassword();
            logger.info("raw password : {}",rawPassword);
            String encPassword=bcryptPasswordEncoder.encode(rawPassword);
            logger.info("enc password : {}",encPassword);

            User newUser = joinDto.toEntity(encPassword);
            logger.info("newUser : {}",newUser.toString());
            userRepository.save(newUser);
            logger.info("회원가입 로직 성공");
            return true;
      }


      public boolean isEmailAvailable(EmailDupCheckDto emailDto) {
            String email = emailDto.getEmail();
            return userRepository.findByEmail(email).isPresent();
      }

      public boolean isNicknameAvailable(NicknameDupCheckDto nicknameDto) {
            String nickname = nicknameDto.getNickname();
            return userRepository.findByNickname(nickname).isPresent();
      }
}
