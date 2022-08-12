package com.umc.coec.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

      @Value("${jwt.secret}")
      private String SECRET;

      @Value("${jwt.issuer}")
      private String ISSUER;

      @Value("${jwt.token-prefix}")
      private String TOKEN_PREFIX;

      @Value("${jwt.header-name}")
      private String HEADER_NAME;

      @Value("${jwt.access-token-expire-length}")
      private  long ACCESS_VALIDITY_IN_MILLISECONDS;

      @Value("${jwt.refresh-token-expire-length}")
      private long REFRESH_VALIDITY_IN_MILLISECONDS;


      //토큰 생성
      public JwtModel createToken(String email){
            Map<String,Object> claims=new HashMap<>();
            claims.put("email",email);

            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date accessDate = new Date(now.getTime()+ACCESS_VALIDITY_IN_MILLISECONDS);
            Date refreshDate = new Date(now.getTime()+REFRESH_VALIDITY_IN_MILLISECONDS);

            return JwtModel.builder()
                        .accessToken(this.generateToken(claims,now,accessDate))
                        .refreshToken(this.generateToken(claims,now,refreshDate))
                        .accessTokenExpirationDate(sdf.format(accessDate))
                        .refreshTokenExpirationDate(sdf.format(refreshDate))
                        .build();
      }

      //토큰 발급
      private  String generateToken(Map<String,Object> claims, Date now, Date expirationDate){
            return JWT.create()
                        .withSubject("coec-jwt-token")
                        .withClaim("email",claims.get("email").toString())
                        .withIssuer(ISSUER)
                        .withIssuedAt(now)
                        .withExpiresAt(expirationDate)
                        .sign(Algorithm.HMAC512(SECRET));
      }

      //쿠키에 토큰 태우기
      public void createCookie(HttpServletResponse response, String token){
            ResponseCookie cookie =
                        ResponseCookie.from(HEADER_NAME, token)
                                    .httpOnly(true)
                                    .sameSite("lax")
                                    .maxAge(ACCESS_VALIDITY_IN_MILLISECONDS)
                                    .path("/")
                                    .build();
            response.addHeader("Set-Cookie",cookie.toString());
      }

      //쿠키에서 토큰 찾아보기
      public String resolveCookie(HttpServletRequest request){
            final Cookie[] cookies = request.getCookies();
            if(cookies==null) return null; //비었으면 null
            for (Cookie cookie : cookies) {
                  if(cookie.getName().equals(HEADER_NAME)) return cookie.getValue();
            }
            return null;
      }

      //토큰 검증하기
      public boolean validateToken(String token){
            try{
                  String rawToken= token.replace(TOKEN_PREFIX,""); //Bearer 붙은 것 없애기
                  Algorithm algorithm = Algorithm.HMAC512(SECRET); //토큰을 만들 때 사용했던 암호화 알고리즘
                  JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(ISSUER).build();
                  DecodedJWT decodedJWT = jwtVerifier.verify(token); //디코딩된 토큰
                  return true;
            }catch(RuntimeException e){
                  log.error(e.getMessage());
                  throw e;
            }
      }

      //토큰에서 claim 모두 가져오기
      public Map<String, Claim> extractAllClaims(String token){
            String rawToken = token.replace(TOKEN_PREFIX, "");
            return JWT.require(Algorithm.HMAC512(SECRET))
                        .build().verify(token).getClaims();
      }

      //토큰에서 특정 key에 대한 claim 추출하기
      public String getClaim(String token, String key){
            return this.extractAllClaims(token).get(key).toString().replaceAll("\"","");
      }

      public String getClaimFromExpiredToken(String token, String key){
            return JWT.decode(token).getClaim(key).toString().replaceAll("\"","");
      }
}
