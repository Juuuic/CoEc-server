package com.umc.coec.dto.join_post;

import com.umc.coec.domain.join_post.JoinPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinPostResDto {
    private String nickname;
    private String profileImgUrl;
    private String comment;

    public JoinPostResDto(JoinPost joinPost) {
        nickname = joinPost.getUser().getNickname();
        profileImgUrl = joinPost.getUser().getProfileImgUrl();
        comment = joinPost.getComment();
    }
}
