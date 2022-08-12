package com.umc.coec.dto.partner_post;

import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.skilled.Skilled;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadPostsResDto {
    private String sportsName;
    private int headCount;
    private String place;
    private LocalDate startDate;
    private LocalDate endDate;
    private String nickname;
    private String profileImgUrl;
    private int interest;
    private String status;

    // entity -> dto
    public ReadPostsResDto(Post post) {
        this.sportsName = post.getSports().getName();
        this.headCount = post.getHeadCount();
        this.place = post.getLocation().getEupMyunDongLi();
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
}
