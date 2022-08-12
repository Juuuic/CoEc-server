package com.umc.coec.domain.user;

import com.umc.coec.domain.BaseTimeEntity;
import com.umc.coec.domain.enums.Gender;
import com.umc.coec.domain.enums.RoleType;
import com.umc.coec.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
//@Table(name = "user_table") //h2 db의 예약어로 User가 있기 때문에 h2 사용할때는 이 어노테이션 활성화
public class User extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Comment("ACTIVE: 활성화,  INACTIVE: 비활성화,  DELETED: 탈퇴")
    private Status status=Status.ACTIVE;


    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false,unique = true)
    private String nickname;

    private String phone;
    private String profileImgUrl;
    private String bio;

    @Builder.Default
    @Comment( "USER / ADMIN ")
    @Enumerated(EnumType.STRING)
    private RoleType roleType = RoleType.USER;

    @Comment(value = "계정 만료 여부")
    @Builder.Default
    private Boolean isAccountNonExpired=true;

    @Comment( "계정 잠금 여부")
    @Builder.Default
    private Boolean isAccountNonLocked=true;

    @Comment( "비밀번호 만료 여부")
    @Builder.Default
    private Boolean isCredentialNonExpired=true;

    @Builder.Default
    @Comment("사용 가능 여부")
    private Boolean isEnabled=true;

    @Builder.Default
    @Comment("실명 인증 여부")
    private Boolean isVerified=false;

}
