package com.umc.coec.domain.sports;

import com.umc.coec.domain.BaseTimeEntity;
import com.umc.coec.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.web.bind.annotation.CookieValue;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Sports extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status=Status.ACTIVE;


    @Comment("운동 종목")
    @Column(nullable = false)
    private String name;

    @Comment("false: 개인종목, true: 단체종목")
    @Column(nullable = false)
    private Boolean isTeamSports=false;
}