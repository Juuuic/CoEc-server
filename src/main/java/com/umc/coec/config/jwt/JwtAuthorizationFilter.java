package com.umc.coec.config.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.umc.coec.config.auth.PrincipalDetailsService;
import com.umc.coec.domain.user.UserOauth;
import com.umc.coec.service.AuthService;
import com.umc.coec.service.UserOauthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Security;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

      private final JwtService jwtService;
      private final UserOauthService userOauthService;
      private final PrincipalDetailsService principalDetailsService;

      public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService, UserOauthService userOauthService, PrincipalDetailsService principalDetailsService) {
            super(authenticationManager);
            this.jwtService = jwtService;
            this.userOauthService = userOauthService;
            this.principalDetailsService = principalDetailsService;
      }

      @Override
      protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
            log.info("=================================================");
            log.info("JwtAuthorizationFilter 시작");

            String accessToken =jwtService.resolveCookie(request);
            String refreshToken= null;
            String email=null;
            log.info("access token : {}",accessToken);

            //access token 검증
            try{
                  if(StringUtils.hasText(accessToken) && jwtService.validateToken(accessToken)){
                        Authentication auth = this.getAuthentication(accessToken);
                        log.info("세션 추가");
                        SecurityContextHolder.getContext().setAuthentication(auth);
                  }
            }catch(TokenExpiredException e){
                  log.error("refresh token expired : {}",e.getMessage());
                  email = jwtService.getClaimFromExpiredToken(accessToken,"email"); //만료된 토큰에서 유저네임 추출
                  log.info("email : {}",email);
                  UserOauth userOauth = userOauthService.findUserOauthByEmail(email);
                  if(!ObjectUtils.isEmpty(userOauth)){
                        refreshToken = userOauth.getRefreshToken(); //db에서 유저네임으로 리프레시 토큰 가져오기
                        log.info("refresh token : {}",refreshToken);
                  }
            }catch(Exception e){
                  SecurityContextHolder.clearContext();
                  log.error("JwtAuthorizationFilter internal error : {}",e.getMessage());
                  return;
            }

            //refresh 토큰으로 access token 발급하기
            if(StringUtils.hasText(refreshToken)){
                  try{
                        try{
                              if (jwtService.validateToken(refreshToken)){
                                    Authentication auth =this.getAuthentication(refreshToken);
                                    SecurityContextHolder.getContext().setAuthentication(auth);

                                    String newAccessToken = jwtService.createToken(email).getAccessToken();
                                    jwtService.createCookie(response, newAccessToken);
                              }
                        }catch(TokenExpiredException e) {
                              log.error("refresh token expired : {}", e.getMessage());
                        }
                  }catch(Exception e){
                        SecurityContextHolder.clearContext();
                        log.error("JwtAuthorizationFilter internal error : {}",e.getMessage());
                        return;
                  }
            }
            chain.doFilter(request,response);
      }

      public Authentication getAuthentication(String token){
            String email = jwtService.getClaim(token,"email");
            UserDetails userDetails = principalDetailsService.loadUserByUsername(email);
            return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
      }
}
