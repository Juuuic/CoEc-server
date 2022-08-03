package com.umc.coec.dto.partner_post;

import com.umc.coec.domain.enums.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DayandTime {
    @Enumerated(EnumType.STRING)
    private String day;

    @Comment("운동 시작 시간")
    private LocalTime startTime;

    @Comment("운동 종료 시간")
    private LocalTime endTime;
}
