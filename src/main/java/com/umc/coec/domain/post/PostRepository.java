package com.umc.coec.domain.post;

import com.umc.coec.domain.skilled.Skilled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where p.status != 'DELETED' and p.division = 'PARTNER'")
    public List<Post> findPartnerPosts();

    @Query("select s from Skilled s where s.sports.id = :sportsId and s.user.id = :userId")
    public Skilled findSkilled(@Param("sportsId") Long sportsId, @Param("userId") Long userId);
}
