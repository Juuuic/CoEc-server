package com.umc.coec.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NicknameDupCheckDto {
      @NotBlank(message = "닉네임은 필수 입력 값입니다.")
      @Pattern(regexp = "^[0-9a-zA-Zㄱ-ㅎ가-힣]{4,10}$",message = "닉네임은 한글, 영문, 숫자 조합으로 4~10자로만 입력 가능합니다.")
      String nickname;
}
