package com.umc.coec.domain.interest;

import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest,Long> {
    public Boolean existsByPostAndUser(Post post, User user);
}
