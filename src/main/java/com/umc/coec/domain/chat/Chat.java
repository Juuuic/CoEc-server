package com.umc.coec.domain.chat;

import com.umc.coec.domain.BaseTimeEntity;
import com.umc.coec.domain.chat_room.ChatRoom;
import com.umc.coec.domain.enums.Status;
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
public class Chat extends BaseTimeEntity {

      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Id
      private Long id;

      private Status status=Status.ACTIVE;

      @JoinColumn(name = "chatRoomId")
      @ManyToOne(fetch = FetchType.EAGER)
      private ChatRoom chatRoom;

      @JoinColumn(name="userId")
      @ManyToOne(fetch = FetchType.EAGER)
      private User user;

      @Column(length = 1000)
      private String contents;

}
