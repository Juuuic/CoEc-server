package com.umc.coec.domain.post;

import com.umc.coec.domain.interest.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT * FROM Post p WHERE p.division != 'PARTENER' and status != 'DELETED' order by p.updatedAt desc", nativeQuery = true)
    public List<Post> findMentoringPostsAll();

    public Post findMentoringPostById(Long id);

    @Query(value = "SELECT i FROM Interest i WHERE i.post.id = :postId")
    public List<Interest> findInterestByPostId(@Param("postId") Long id);

}
