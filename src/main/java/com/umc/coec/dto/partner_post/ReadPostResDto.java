package com.umc.coec.dto.partner_post;

import com.umc.coec.domain.enums.Gender;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.skilled.Skilled;
import com.umc.coec.domain.time.Time;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadPostResDto {
    private String nickname;
    private String profileImgUrl;
    private List<String> purposes = new ArrayList<>();
    private LocalDate updatedAt;
    private String sportsName;

    private int headCount;
    private String place;

    private LocalDate startDate;
    private LocalDate endDate;

    // 요일별 시간
    private List<DayandTime> dayandTimes = new ArrayList<>();

    private int skilled;
    private int year;
    private int month;
    private String experience;

    private int ageWanted;
    private Gender genderWanted;

    private String contents;
    private int interest;

    // 관심 클릭 여부
    private Boolean likeState;

    private String status;

    // entity -> dto
    public ReadPostResDto(Post post, Skilled skilled) {
        this.nickname = post.getUser().getNickname();
        this.profileImgUrl = post.getUser().getProfileImgUrl();
        for (int i = 0; i < post.getPurposes().size(); i++)
            this.purposes.add(post.getPurposes().get(i).getContents());
        this.updatedAt = LocalDate.from(post.getUpdatedAt());
        this.sportsName = post.getSports().getName();
        this.headCount = post.getHeadCount();
        this.place = post.getLocation().getEupMyunDongLi();

        this.startDate = post.getStartDate();
        this.endDate = post.getEndDate();

        for (int i = 0; i < post.getTimes().size(); i++) {
            Time time = post.getTimes().get(i);
            this.dayandTimes.add(new DayandTime(time.getDay(), time.getStartTime(), time.getEndTime()));
        }

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