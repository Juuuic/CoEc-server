package com.umc.coec.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@Slf4j
public class SecurityConfig {

      @Value("${jwt.header-name}")
      private String HEADER_NAME;
      private final JwtHttpConfigurer jwtHttpConfigurer;

      @Bean
      public HttpFirewall defaultHttpFireWall(){
            return new DefaultHttpFirewall();
      }


      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

            http
                        .csrf().disable()
                        .formLogin().disable()
                        .httpBasic().disable()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http
                        .headers()
                        .cacheControl().disable()
                        .contentTypeOptions().disable()
                        .frameOptions().sameOrigin()
                        .httpStrictTransportSecurity().disable();


            http
                        .authorizeRequests()

                        .antMatchers("/api/v1/test/user").authenticated()
                        .antMatchers("/api/v1/test/admin").access("hasRole('ROLE_ADMIN')")

                        .antMatchers("/api/v1/posts/**","/api/v1/chat/**").authenticated()
                        .antMatchers("/api/v1/admin/**").access("hasRole('ROLE_ADMIN')")
                        .anyRequest().permitAll();



            http
                        .logout()
                        .logoutUrl("/api/v1/auth/logout")
                        .deleteCookies(HEADER_NAME)
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true);

            http
                        .apply(jwtHttpConfigurer);

//            http
//                        .oauth2Login()
//                        .userInfoEndpoint().userService()
//            http
//                        .exceptionHandling()
//                        .accessDeniedHandler()

            return http.build();
      }
}


