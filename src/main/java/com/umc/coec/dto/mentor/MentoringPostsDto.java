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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MentoringPostsDto {

    //멘토멘티 게시글 목록 페이지 Dto
    //일단은 다 만들고 나중에 디자인 따라서 변경 필요
    //지금은 프로토타입을 기준으로 했음
    private String division;
    private String sportsName;
    private int headCount;
    private String title;
    private String location;
    private Gender genderWanted;
    private int interests;
    private String status;
    private Boolean like;

    public MentoringPostsDto(Post entity) {
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
        this.genderWanted = entity.getGenderWanted();
        this.interests = entity.getInterests().size();
        switch (entity.getStatus()) {
            case ACTIVE:
                this.status = "모집 중";
                break;
            case INACTIVE:
                this.status = "모집 완료";
                break;
        }
    }
}
