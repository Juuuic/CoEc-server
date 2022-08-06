package com.umc.coec.domain.time;

import com.umc.coec.domain.BaseTimeEntity;
import com.umc.coec.domain.enums.Day;
import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.post.Post;
import com.umc.coec.dto.partner_post.UpdatePostReqDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
//@Table(name = "time_table") //h2 db의 예약어로 Time이 있기 때문에 h2 사용할때는 이 어노테이션 활성화
public class Time extends BaseTimeEntity  {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;


    @Enumerated(EnumType.STRING)
    private Status status=Status.ACTIVE;

    @Enumerated(EnumType.STRING)
    private Day day;

    @Comment("운동 시작 시간")
    private LocalTime startTime;


    @Comment("운동 종료 시간")
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    public Time update(int i, UpdatePostReqDto updatePostReqDto) {
        day = updatePostReqDto.getDayandTimes().get(i).getDay();
        startTime = updatePostReqDto.getDayandTimes().get(i).getStartTime();
        endTime = updatePostReqDto.getDayandTimes().get(i).getEndTime();
        status = updatePostReqDto.getStatus().equals("모집중") ? Status.ACTIVE : Status.INACTIVE;

        return this;
    }
}
