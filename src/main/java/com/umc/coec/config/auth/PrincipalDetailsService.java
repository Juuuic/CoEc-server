package com.umc.coec.config.auth;

import com.umc.coec.domain.user.User;
import com.umc.coec.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {
      private final UserRepository userRepository;

      @Override
      public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            User principal = userRepository
                        .findByEmail(email)
                        .orElseThrow(()->{
                              log.info("해당 이메일을 사용하는 사용자가 존재하지 않음.");
                              return new UsernameNotFoundException("해당 이메일을 사용하는 사용자가 존재하지 않습니다.");
                        });

            log.info("principal : {}",principal);

            return new PrincipalDetails(principal);
      }
}
