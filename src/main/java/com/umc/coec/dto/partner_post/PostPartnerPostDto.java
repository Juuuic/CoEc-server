package com.umc.coec.dto.partner_post;

import com.umc.coec.domain.enums.Day;
import com.umc.coec.domain.enums.Gender;
import com.umc.coec.domain.enums.Division;
import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.location.Location;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.purpose.Purpose;
import com.umc.coec.domain.skilled.Skilled;
import com.umc.coec.domain.sports.Sports;
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
public class PostPartnerPostDto {
    private String sportsName;

    private int headCount;

    private String siDo;
    private String siGunGu;
    private String eupMyunDongLi;

    private LocalDate startDate;
    private LocalDate endDate;

    // 요일별 시간
    private List<DayandTime> dayandTimes;

    private int ageWanted;
    private String genderWanted;

    private int skilled;
    private int year;
    private int month;
    private String experience;

    private String contents;

    private List<String> purposes = new ArrayList<>();

    public Post toPostEntity() {
        Sports sports = new Sports();
        sports.setName(sportsName);
        Location location = new Location();
        location.setSiDo(siDo); location.setSiGunGu(siGunGu); location.setEupMyunDongLi(eupMyunDongLi);

        return Post.builder()
                .division(Division.PARTNER)
                .status(Status.ACTIVE)
                .sports(sports)
                .headCount(headCount)
                .location(location)
                .startDate(startDate)
                .endDate(endDate)
                .ageWanted(ageWanted)
                .genderWanted(genderWanted.equals("Male")? Gender.MALE : Gender.FEMALE)
                .contents(contents)
                .build();
    }

    public Skilled toSkilledEntity(Sports sports) {
        return Skilled.builder()
                .skilled(skilled)
                .year(year)
                .month(month)
                .experience(experience)
                .sports(sports)
                .status(Status.ACTIVE)
                .build();
    }

    public Purpose toPurposeEntity(int i, Post post) {
        return Purpose.builder()
                .contents(purposes.get(i))
                .status(Status.ACTIVE)
                .post(post)
                .build();
    }

    public Time toTimeEntity(int i, Post post) {
        return Time.builder()
                .day(dayandTimes.get(i).getDay().equals("월")? Day.MON:
                        dayandTimes.get(i).getDay().equals("화")? Day.TUE:
                                dayandTimes.get(i).getDay().equals("수")? Day.WED:
                                        dayandTimes.get(i).getDay().equals("목")? Day.THU:
                                                dayandTimes.get(i).getDay().equals("금")? Day.FRI:
                                                        dayandTimes.get(i).getDay().equals("토")? Day.SAT: Day.SUN
                )
                .startTime(dayandTimes.get(i).getStartTime())
                .endTime(dayandTimes.get(i).getEndTime())
                .post(post)
                .status(Status.ACTIVE)
                .build();
    }
}
