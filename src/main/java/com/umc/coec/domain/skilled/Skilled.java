package com.umc.coec.domain.skilled;

import com.umc.coec.domain.BaseTimeEntity;
import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.sports.Sports;
import com.umc.coec.domain.user.User;
import com.umc.coec.dto.partner.PartnerPostReqDto;
import com.umc.coec.dto.partner.PartnerPostResDto;
import com.umc.coec.dto.partner.UpdatePostReqDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Skilled extends BaseTimeEntity  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status=Status.ACTIVE;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "sportsId")
    private Sports sports;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    private int year;
    private int month;

    @Comment("점수 0~10")
    @Column(nullable = false)
    private int skilled;

    @Comment("경력, 수상이력 등 종목 관련 자기소개")
    @Column(length = 1000)
    private String experience;

    public void update(PartnerPostReqDto partnerPostReqDto) {
        this.setSkilled(partnerPostReqDto.getSkilled());
        this.setYear(partnerPostReqDto.getYear());
        this.setMonth(partnerPostReqDto.getMonth());
        this.setExperience(partnerPostReqDto.getExperience());

        this.setStatus(partnerPostReqDto.getStatus().equals("모집중") ? Status.ACTIVE : Status.INACTIVE);
    }
}
