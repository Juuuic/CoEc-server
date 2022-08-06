package com.umc.coec.dto.partner_post;

import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.skilled.Skilled;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPartnerPostsDto {
    private String sportsName;
    private int headCount;
    private String place;
    private int skilled;
    private String nickname;
    private String profileImgUrl;
    private List<String> purposes = new ArrayList<>();
    private int interest;
    private String status;

    // entity -> dto
    public GetPartnerPostsDto(Post post, Skilled skilled) {
        this.sportsName = post.getSports().getName();
        this.headCount = post.getHeadCount();
        this.place = post.getLocation().getEupMyunDongLi();
        this.nickname = post.getUser().getNickname();
        this.profileImgUrl = post.getUser().getProfileImgUrl();
        this.skilled = skilled.getSkilled();
        // 운동 목적 몇 개까지 보여줄지 논의 필요
        for (int i = 0; i < post.getPurposes().size(); i++)
            this.purposes.add(post.getPurposes().get(i).getContents());
        this.interest = post.getInterests().size();
        switch (post.getStatus()) {
            case ACTIVE: this.status = "모집중"; break;
            case INACTIVE: this.status = "모집완료";
        }
    }
}
