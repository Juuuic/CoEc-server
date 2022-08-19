package com.umc.coec.domain.post;

import com.umc.coec.domain.interest.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.umc.coec.domain.skilled.Skilled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {


    @Query(value = "SELECT * FROM Post p WHERE p.division != 'PARTENER' and status != 'DELETED' order by p.updatedAt desc", nativeQuery = true)
    List<Post> findMentoringPostsAll();

    Post findMentoringPostById(Long id);

    @Query(value = "SELECT i FROM Interest i WHERE i.post.id = :postId")
    List<Interest> findInterestByPostId(@Param("postId") Long id);
    
    // 페이징하려면 아예 새로 만들어 할 수도
    @Query(nativeQuery = true, value = "select * from Post p where p.status != 'DELETED' and p.division = 'PARTNER' order by p.updatedAt desc limit 9")
    List<Post> findPartnerPosts();

    @Query("select p from Post p where p.id = :postId and p.status != 'DELETED' and p.division = 'PARTNER'")
    Post findPartnerPost(@Param("postId") Long postId);

    @Query("select p from Post p where p.id = :postId and p.status = 'ACTIVE'")
    Post findJoinablePost(@Param("postId")Long postId);
}
