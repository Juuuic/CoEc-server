package com.umc.coec.domain.user;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class UserOauth {

      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Id
      private Long id;

      @Column(length = 1000)
      private String refreshToken;

      private String email;
}
