package com.umc.coec.domain.post;

import com.umc.coec.domain.interest.Interest;
import com.umc.coec.domain.skilled.Skilled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {
    // 9개만 받아와야 하는 코드 추가 필요 (페이징하려면 아예 새로 만들어 할 수도)
    @Query("select p from Post p where p.status != 'DELETED' and p.division = 'PARTNER'")
    public List<Post> findPartnerPosts();

    @Query("select s from Skilled s where s.sports.id = :sportsId and s.user.id = :userId")
    public Skilled findSkilled(@Param("sportsId") Long sportsId, @Param("userId") Long userId);

    public Post findPartnerPostById(Long id);

    @Query("select i from Interest i where i.post.id = :postId")
    public List<Interest> findInterestsByPostId(@Param("postId") Long postId);
}
