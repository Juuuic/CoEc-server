package com.umc.coec.domain.report;

import com.umc.coec.domain.BaseTimeEntity;
import com.umc.coec.domain.enums.Category;
import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Report extends BaseTimeEntity  {

      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Id
      private Long id;

      private Status status=Status.ACTIVE;

      @JoinColumn(name="userId")
      @ManyToOne(fetch = FetchType.EAGER)
      private User user;

      @JoinColumn(name="postId")
      @ManyToOne(fetch = FetchType.EAGER)
      private Post post;

      @Enumerated(EnumType.STRING)
      private Category category;

      private String contents;

}
