package com.umc.coec.dto.mentor;

import com.umc.coec.domain.enums.Division;
import com.umc.coec.domain.enums.Gender;
import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.interest.Interest;
import com.umc.coec.domain.join_post.JoinPost;
import com.umc.coec.domain.location.Location;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.sports.Sports;
import com.umc.coec.domain.time.Time;
import com.umc.coec.domain.user.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MentoringPostResponseDto {

    //멘토멘티 포스트 상세 조회 Dto
    //나중에 디자인 따라서 변경 필요
    private String division;
    private String sportsName;
    private int headCount;
    private String title;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private Gender genderWanted;
    private String contents;
    private int interests;
    private String status;
    private String nickName;



    public MentoringPostResponseDto(Post entity) {
        switch (entity.getDivision()) {
            case MENTEE:
                this.division = "멘토";
                break;
            case MENTOR:
                this.division = "멘티";
                break;
        }
        this.sportsName = entity.getSports().getName();
        this.headCount = entity.getHeadCount();
        this.title = entity.getTitle();
        this.location = entity.getLocation().getEupMyunDongLi();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.genderWanted = entity.getGenderWanted();
        this.contents = entity.getContents();
        this.interests = entity.getInterests().size();
        switch (entity.getStatus()) {
            case ACTIVE:
                this.status = "모집 중";
                break;
            case INACTIVE:
                this.status = "모집 완료";
                break;
        }
        this.nickName = entity.getUser().getNickname();


    }
}
