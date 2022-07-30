package com.umc.coec.domain.chat_room;

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
@Table( //등록자 - 신청자 사이에 채팅방은 하나만 가능
            uniqueConstraints = {
                        @UniqueConstraint(
                                    name="chat_room_uk",
                                    columnNames = {"postUserId","joinUserId"}
                        )
            }
)
public class ChatRoom extends BaseTimeEntity {

      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Id
      private Long id;


      @Enumerated(EnumType.STRING)
      private Status postUserStatus=Status.ACTIVE;

      @Enumerated(EnumType.STRING)
      private Status joinUserStatus=Status.ACTIVE;

      @Comment("글 등록자")
      @JoinColumn(name="postUserId")
      @ManyToOne(fetch = FetchType.EAGER)
      private User postUser;

      @Comment("신청자")
      @JoinColumn(name="joinUserId")
      @ManyToOne(fetch = FetchType.EAGER)
      private User joinUser;

      @JoinColumn(name="postId")
      @ManyToOne(fetch = FetchType.LAZY)
      private Post post;

}
