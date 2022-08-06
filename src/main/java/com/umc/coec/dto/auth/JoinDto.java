package com.umc.coec.dto.auth;

import com.umc.coec.domain.enums.Gender;
import com.umc.coec.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinDto {


      @NotBlank(message = "이메일은 필수 입력 값입니다.")
      @Email(message = "이메일 형식에 맞지 않습니다.")
      private String email;

      @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
      @Pattern(regexp="^[a-zA-Z0-9]{8,20}$",message ="비밀번호는 8~20자의 영문 대소문자와 숫자로만 입력 가능합니다.")
      private String password;

      @NotBlank(message = "확인 비밀번호는 필수 입력 값입니다.")
      @Pattern(regexp="^[a-zA-Z0-9]{8,20}$",message ="비밀번호는 8~20자의 영문 대소문자와 숫자로만 입력 가능합니다.")
      private String password2;

      @NotBlank(message = "이름은 필수 입력 값입니다.")
      @Pattern(regexp = "^[가-힣]{1,5}$")
      private  String name;

      @NotBlank(message = "닉네임은 필수 입력 값입니다.")
      @Pattern(regexp = "^[0-9a-zA-Zㄱ-ㅎ가-힣]{4,10}$",message = "닉네임은 한글, 영문, 숫자 조합으로 4~10자로만 입력 가능합니다.")
      private String nickname;

      @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",message = "휴대전화 형식이 올바르지 않습니다.")
      private String phone;

      @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일 형식이 올바르지 않습니다.")
      private String birthDate;

      @NotBlank(message = "성별은 필수 선택 값입니다.")
      private String gender;

      //생년월일 str -> int[]로 쪼개기
      private Integer[] splitBirthDate(String birthDate){
            String[] splitedBirthDateStrArr=birthDate.split("-");
            Integer[] splitedBirthDateIntArr=new Integer[3];
            for (int i = 0; i <3; i++) {
                  String s = splitedBirthDateStrArr[i];
                  splitedBirthDateIntArr[i]=Integer.parseInt(s);
            }
            return splitedBirthDateIntArr;
      }

      public User toEntity(String encPassword){
            Integer[] SBDA=splitBirthDate(birthDate); //splitedBirthDateIntArr
            return User.builder()
                        .email(email)
                        .password(encPassword)
                        .name(name)
                        .nickname(nickname)
                        .phone(phone)
                        .birthDate(LocalDate.of(SBDA[0],SBDA[1],SBDA[2]))
                        .gender( gender.equals("Male")? Gender.MALE : Gender.FEMALE )
                        .build();
      }
}
