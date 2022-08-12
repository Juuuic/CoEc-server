package com.umc.coec.config.jwt;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.coec.config.auth.PrincipalDetails;
import com.umc.coec.dto.auth.LoginDto;
import com.umc.coec.service.UserOauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

      private final AuthenticationManager authenticationManager;
      private final UserOauthService userOauthService;
      private final JwtService jwtService;


      @Override
      public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
            log.info("=================================================");
            log.info("JwtAuthenticationFilter - attemptAuthentication : 로그인 시도중");
            ObjectMapper om = new ObjectMapper();
            try{
                  log.info("id, pw json 파싱 ");
                  LoginDto loginDto = om.readValue(request.getInputStream(),LoginDto.class);
                  log.info("Login dto : {}",loginDto);

                  UsernamePasswordAuthenticationToken authenticationToken =
                              new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword());

                  Authentication authentication = authenticationManager.authenticate(authenticationToken);

                  PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

                  log.info("로그인 성공");
                  log.info("=================================================");
                  return authentication;

            }catch(UsernameNotFoundException e){
                  log.error("유저 못 찾음 : {}",e.getMessage());
            } catch (IOException e) {
                  log.error("에러 발생");
                  e.printStackTrace();
            }

            log.info("로그인 실패");
            log.info("=================================================");
            return null;
      }

      @Override
      protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
            log.info("=================================================");
            log.info("JwtAuthenticationFilter - successfulAuthentication : 로그인 후 처리중 (jwt 토큰 만들기");
            PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
            String email = principalDetails.getUsername();


            JwtModel jwtModel = jwtService.createToken(email);
            log.info("refresh token : {}",jwtModel.getRefreshToken());
            jwtService.createCookie(response, jwtModel.getAccessToken()); //쿠키에 액세스 토큰 태우기
            userOauthService.deleteUserOauth(email); //기존 리프레시 토큰 지우기
            userOauthService.insertUserOauth(email,jwtModel);

            ObjectMapper objectMapper= new ObjectMapper(); //json에 담을 매퍼
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("url","/"); //로그인 전에 있던 곳으로 돌아가기 위해 세션에서 기존 url 받아서 넣기

            ResponseEntity<Map<String,String>> responseEntity = new ResponseEntity<>(dataMap,HttpStatus.OK);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(objectMapper.writeValueAsString(responseEntity));
            response.getWriter().flush();

            log.info("json 리턴 ");
            log.info("JwtAuthenticationFilter 종료 ");
            log.info("=================================================");
      }
}
