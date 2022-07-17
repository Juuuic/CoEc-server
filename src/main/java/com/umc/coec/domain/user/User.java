package com.umc.coec.domain.user;

import com.umc.coec.domain.enums.Gender;
import com.umc.coec.domain.enums.Role;
import com.umc.coec.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long userId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Temporal(TemporalType.DATE) // 년/월/일 타입 (이 어노테이션이 없으면 시/분/초 까지 사용됨)
    @Column(nullable = false)
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phone;

    @Column(nullable = false)
    private String nickname;

    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String bio;

    private Boolean isAccountExpired;
    private Boolean isAccountLocked;
    private Boolean isCredentialExpired;
    private Boolean isEnabled;
    private Boolean isVerified;

//유저의 주 활동지역 ? 같은 부분 사용할때 쓰려고 둔 것 같은데  등록 글에서 locationId를 사용하면 여기서도 사용할 필요가 있을까? 논의 필요할 듯
//    private long locationId;




}