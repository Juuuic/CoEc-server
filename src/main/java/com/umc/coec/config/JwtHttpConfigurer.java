package com.umc.coec.config;

import com.umc.coec.config.auth.PrincipalDetailsService;
import com.umc.coec.config.jwt.JwtAuthenticationFilter;
import com.umc.coec.config.jwt.JwtAuthorizationFilter;
import com.umc.coec.config.jwt.JwtService;
import com.umc.coec.service.UserOauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CorsFilter;

@Component
@RequiredArgsConstructor
public class JwtHttpConfigurer extends AbstractHttpConfigurer<JwtHttpConfigurer, HttpSecurity> {

      private final CorsFilter corsFilter;
      private final UserOauthService userOauthService;
      private final JwtService jwtService;
      private final PrincipalDetailsService principalDetailsService;



      @Override
      public void configure(HttpSecurity http) throws Exception {
            final AuthenticationManager authenticationManager=http.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager,userOauthService,jwtService);
            jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");
            JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager,jwtService,userOauthService,principalDetailsService);
            http
                        .addFilter(corsFilter)
                        .addFilter(jwtAuthenticationFilter)
                        .addFilter(jwtAuthorizationFilter);
      }
}
