package com.umc.coec.domain.post;

import com.umc.coec.domain.BaseTimeEntity;
import com.umc.coec.domain.enums.Division;
import com.umc.coec.domain.enums.Gender;
import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.interest.Interest;
import com.umc.coec.domain.join_post.JoinPost;
import com.umc.coec.domain.location.Location;
import com.umc.coec.domain.purpose.Purpose;
import com.umc.coec.domain.sports.Sports;
import com.umc.coec.domain.time.Time;
import com.umc.coec.domain.user.User;
import com.umc.coec.dto.partner.PartnerPostRequestDto;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Post extends BaseTimeEntity  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status=Status.ACTIVE;

    @Comment("등록 글 구분: 파트너 매칭/ 멘토 /멘티")
    @Column
    @Enumerated(EnumType.STRING)
    private Division division;


    @Comment("모집글 등록한 사람")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "sportsId")
    private Sports sports;

    @Comment("시작 날짜")
    @Column(nullable = false)
    private LocalDate startDate;


    @Comment("종료 날짜")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "locationId", nullable = false)
    private Location location;

    @Comment("구하는 사람 수")
    @Column(nullable = false)
    private int headCount;

    private String title;

    @Column(length = 1000)
    private String contents;

    @Comment("원하는 나이대")
    @Column(nullable = false)
    private int ageWanted;

    @Comment("원하는 성별")
    @Enumerated(EnumType.STRING)
    private Gender genderWanted;

    @OneToMany(fetch= FetchType.LAZY, mappedBy = "post")
    private List<JoinPost> joinPosts = new ArrayList<>();

    @OneToMany(fetch= FetchType.LAZY, mappedBy = "post")
    private List<Purpose> purposes = new ArrayList<>();

    @OneToMany(fetch= FetchType.LAZY, mappedBy = "post")
    private List<Time> times = new ArrayList<>();

    @OneToMany(fetch= FetchType.LAZY, mappedBy = "post")
    private List<Interest> interests = new ArrayList<>();

    public void update(PartnerPostRequestDto partnerPostRequestDto) {
        // 입력 필수인 것들은 null 걸러내서 기존 거 그대로
        int i;

        if (partnerPostRequestDto.getSportsName() != null)
            sports.setName(partnerPostRequestDto.getSportsName());

        this.setHeadCount(partnerPostRequestDto.getHeadCount());

        location.setSiDo(partnerPostRequestDto.getSiDo());
        location.setSiGunGu(partnerPostRequestDto.getSiGunGu());
        location.setEupMyunDongLi(partnerPostRequestDto.getEupMyunDongLi());

        if (partnerPostRequestDto.getStartDate() != null)
            this.setStartDate(partnerPostRequestDto.getStartDate());
        this.setEndDate(partnerPostRequestDto.getEndDate());

        this.setAgeWanted(partnerPostRequestDto.getAgeWanted());
        this.setGenderWanted(partnerPostRequestDto.getGenderWanted());

        this.setContents(partnerPostRequestDto.getContents());
        this.setStatus(partnerPostRequestDto.getStatus().equals("모집중") ? Status.ACTIVE : Status.INACTIVE);

        // 모집중 or 모집완료에 따라 status 변경
        sports.setStatus(partnerPostRequestDto.getStatus().equals("모집중") ? Status.ACTIVE : Status.INACTIVE);
        location.setStatus(partnerPostRequestDto.getStatus().equals("모집중") ? Status.ACTIVE : Status.INACTIVE);

        for (i = 0; i < joinPosts.size(); i++)
            joinPosts.get(i).setStatus(partnerPostRequestDto.getStatus().equals("모집중") ? Status.ACTIVE : Status.INACTIVE);
        // 일단 모집완료된 글에는 관심 못 누르게 설정
        for (i = 0; i < interests.size(); i++)
            interests.get(i).setStatus(partnerPostRequestDto.getStatus().equals("모집중") ? Status.ACTIVE : Status.INACTIVE);
    }
}
