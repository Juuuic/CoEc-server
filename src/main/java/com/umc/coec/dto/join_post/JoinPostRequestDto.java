package com.umc.coec.dto.join_post;

import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.join_post.JoinPost;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinPostRequestDto {
    @NotBlank(message = "하고 싶은 말은 필수로 입력해야 합니다.")
    private String comment;

    public JoinPost toEntity(Post post, User user) {
        return JoinPost.builder()
                .status(Status.ACTIVE)
                .post(post)
                .user(user)
                .comment(comment)
                .build();
    }
}
