package com.umc.coec.domain.join_post;

import com.umc.coec.domain.BaseTimeEntity;
import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table( //신청자는 한 글에 대해 한번만 신청이 가능하도록 제한
            uniqueConstraints = {
                        @UniqueConstraint(
                                    name="join_post_uk",
                                    columnNames = {"postId","userId"}
                        )
            }
)
public class JoinPost extends BaseTimeEntity  {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status=Status.ACTIVE;

    @Comment("등록 글")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    @Comment("신청자")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Comment("글 등록자에게 하고싶은 말")
    @Column(nullable = false)
    private String comment;

}
