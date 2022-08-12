package com.umc.coec.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtModel implements Serializable {
      private String accessToken; //액세스 토큰
      private String accessTokenExpirationDate; //액세스 토큰 만료 시간
      private String refreshToken; //리프레시 토큰
      private String refreshTokenExpirationDate; //리프레시 토큰 만료 시간
}
