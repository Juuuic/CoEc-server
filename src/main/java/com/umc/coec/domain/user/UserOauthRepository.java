package com.umc.coec.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOauthRepository extends JpaRepository<UserOauth, Long> {

      void deleteByEmail(String email);
      UserOauth findByEmail(String email);
}
