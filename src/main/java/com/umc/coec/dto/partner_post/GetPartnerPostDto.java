package com.umc.coec.dto.partner_post;

import com.umc.coec.domain.enums.Gender;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.skilled.Skilled;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPartnerPostDto {
    private String nickname;
    private String profileImgUrl;
    private List<String> purposes = new ArrayList<>();
    private String sportsName;

    private int headCount;
    private String place;

    private LocalDate startDate;
    private LocalDate endDate;
    @Comment("요일별 시간")
    private List<DayandTime> dayandTimes;

    private int skilled;
    private int year;
    private int month;
    @Column(length = 1000)
    private String experience;

    private int ageWanted;
    private Gender genderWanted;

    @Column(length = 1000)
    private String contents;
    private int interest;
    @Comment("관심 클릭 여부")
    private Boolean likeState;

    private String status;

    // entity -> dto
    public GetPartnerPostDto(Post post, Skilled skilled) {
        this.nickname = post.getUser().getNickname();
        this.profileImgUrl = post.getUser().getProfileImgUrl();
        for (int i = 0; i < post.getPurposes().size(); i++)
            this.purposes.add(post.getPurposes().get(i).getContents());
        this.sportsName = post.getSports().getName();
        this.headCount = post.getHeadCount();
        this.place = post.getLocation().getEupMyunDongLi();

        this.startDate = post.getStartDate();
        this.endDate = post.getEndDate();

        this.skilled = skilled.getSkilled();
        this.year = skilled.getYear();
        this.month = skilled.getMonth();
        this.experience = skilled.getExperience();

        this.ageWanted = post.getAgeWanted();
        this.genderWanted = post.getGenderWanted();

        this.contents = post.getContents();

        this.interest = post.getInterests().size();

        switch (post.getStatus()) {
            case ACTIVE: this.status = "모집중"; break;
            case INACTIVE: this.status = "모집완료";
        }
    }
}
