package com.umc.coec.dto.partner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.umc.coec.domain.enums.Day;
import com.umc.coec.domain.enums.Gender;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.purpose.Purpose;
import com.umc.coec.domain.skilled.Skilled;
import com.umc.coec.domain.time.Time;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PartnerPostResponseDto {
    private String nickname;
    private String profileImgUrl;
    private List<ExecPurpose> purposes;
    private LocalDate updatedAt;
    private String sportsName;

    private int headCount;

    private String siDo;
    private String siGunGu;
    private String eupMyunDongLi;

    private LocalDate startDate;
    private LocalDate endDate;

    // 요일별 시간
    private List<DayandTime> dayandTimes;

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

    // entity -> 게시물 목록 dto
    public PartnerPostResponseDto(Post post) {
        this.sportsName = post.getSports().getName();
        this.headCount = post.getHeadCount();
        this.siDo = post.getLocation().getSiDo();
        this.siGunGu = post.getLocation().getSiGunGu();
        this.eupMyunDongLi = post.getLocation().getEupMyunDongLi();
        this.startDate = post.getStartDate();
        this.endDate = post.getEndDate();
        this.nickname = post.getUser().getNickname();
        this.profileImgUrl = post.getUser().getProfileImgUrl();
        this.interest = post.getInterests().size();
        switch (post.getStatus()) {
            case ACTIVE: this.status = "모집중"; break;
            case INACTIVE: this.status = "모집완료";
        }
    }

    // entity -> 게시물 dto
    public PartnerPostResponseDto(Post post, Skilled skilled) {
        this.nickname = post.getUser().getNickname();
        this.profileImgUrl = post.getUser().getProfileImgUrl();
        this.purposes = ExecPurpose.getPurposes(post.getPurposes());
        this.updatedAt = LocalDate.from(post.getUpdatedAt());
        this.sportsName = post.getSports().getName();
        this.headCount = post.getHeadCount();
        this.siDo = post.getLocation().getSiDo();
        this.siGunGu = post.getLocation().getSiGunGu();
        this.eupMyunDongLi = post.getLocation().getEupMyunDongLi();

        this.startDate = post.getStartDate();
        this.endDate = post.getEndDate();

        this.dayandTimes = DayandTime.getDayandTimes(post.getTimes());

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

    @Getter
    static class ExecPurpose {
        private String contents;

        static List<ExecPurpose> getPurposes(List<Purpose> purposes) {
            List<ExecPurpose> execPurposes = new ArrayList<>();
            purposes.forEach(purpose -> execPurposes.add(new ExecPurpose(purpose)));
            return execPurposes;
        }

        public ExecPurpose(Purpose purpose) {
            this.contents = purpose.getContents();
        }
    }

    @Getter
    static class DayandTime {
        private Day day;
        private LocalTime startTime;
        private LocalTime endTime;

        static List<DayandTime> getDayandTimes(List<Time> times) {
            List<DayandTime> dayandTimes = new ArrayList<>();
            times.forEach(time -> dayandTimes.add(new DayandTime(time)));
            return dayandTimes;
        }

        public DayandTime(Time time) {
            this.day = time.getDay();
            this.startTime = time.getStartTime();
            this.endTime = time.getEndTime();
        }
    }
}
