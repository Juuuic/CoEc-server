package com.umc.coec.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

      private final Logger logger= LoggerFactory.getLogger(getClass());

      @Bean
      public HttpFirewall defaultHttpFireWall(){
            return new DefaultHttpFirewall();
      }


      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

            http
                        .csrf().disable();

            http
                        .authorizeRequests()
                        .antMatchers("/api/v1/posts/**","/api/vi/chat/**").authenticated()
                        .anyRequest().permitAll();
            http
                        .formLogin()
                        .loginProcessingUrl("/api/v1/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password");

            http
                        .sessionManagement()
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true);

//            http
//                        .oauth2Login()
//                        .userInfoEndpoint().userService()
//            http
//                        .exceptionHandling()
//                        .accessDeniedHandler()

            return http.build();


      }





}
