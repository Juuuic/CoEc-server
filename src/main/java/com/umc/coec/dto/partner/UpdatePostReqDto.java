package com.umc.coec.dto.partner;

import com.umc.coec.domain.enums.Gender;

import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.purpose.Purpose;
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

// 삭제 예정

public class UpdatePostReqDto {

    private List<String> purposes = new ArrayList<>();
    private String sportsName;

    private int headCount;
    private String siDo;
    private String siGunGu;
    private String eupMyunDongLi;

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

    private String status;

    public Purpose toPurposeEntity(int i, Post post) {
        return Purpose.builder()
                .contents(purposes.get(i))
                .status(post.getStatus())
                .post(post)
                .build();
    }
    public Time toTimeEntity(int i, Post post) {
        return Time.builder()
                .day(dayandTimes.get(i).getDay())
                .startTime(dayandTimes.get(i).getStartTime())
                .endTime(dayandTimes.get(i).getEndTime())
                .post(post)
                .status(post.getStatus())
                .build();
    }
}
