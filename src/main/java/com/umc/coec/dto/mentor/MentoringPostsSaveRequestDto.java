package com.umc.coec.dto.mentor;

import com.umc.coec.domain.enums.Division;
import com.umc.coec.domain.enums.Gender;
import com.umc.coec.domain.enums.Status;
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
public class MentoringPostsSaveRequestDto {

    private String divisionName;
    private String sportsName;
    private int headCount;
    private String eupMyunDongLi;
    private String siGunGu;
    private String siDo;
    private LocalDate startDate;
    private LocalDate endDate;
    private String genderWantedName;
    private String contents;
    private String title;



    /*
    TODO
       요일별로 시간 설정하는 부분도 들어가야하지 않나요 ??
     */
    
    public Post toEntity() {
        Location location = new Location();
        location.setEupMyunDongLi(eupMyunDongLi);
        location.setSiGunGu(siGunGu);
        location.setSiDo(siDo);
        Sports sports = new Sports();
        sports.setName(sportsName);


        return Post.builder()
                .division(Division.valueOf(divisionName))
                .sports(sports)
                .headCount(headCount)
                .location(location)
                .startDate(startDate)
                .endDate(endDate)
                .genderWanted(Gender.valueOf(genderWantedName))
                .contents(contents)
                .title(title)
                .status(Status.ACTIVE)
                .build();
    }
}
