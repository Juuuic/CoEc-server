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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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


}
