package com.umc.coec.domain.join_post;

import com.umc.coec.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinPostRepository extends JpaRepository<JoinPost,Long> {
    public JoinPost findByIdAndUser(Long id, User user);
}
