package com.umc.coec.domain.interest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest,Long> {
    @Query("select i from Interest i where i.post.id = :postId")
    public List<Interest> findInterestsByPostId(@Param("postId") Long postId);
}
