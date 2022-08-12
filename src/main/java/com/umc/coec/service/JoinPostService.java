package com.umc.coec.service;

import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.join_post.JoinPost;
import com.umc.coec.domain.join_post.JoinPostRepository;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.post.PostRepository;
import com.umc.coec.dto.join_post.JoinPostReqDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JoinPostService {
    private final Logger logger= LoggerFactory.getLogger(getClass());
    private final PostRepository postRepository;
    private final JoinPostRepository joinPostRepository;

    public boolean createJoinPost(Long postId, JoinPostReqDto joinPostReqDto/*, User user*/) {
        Post post = postRepository.findPartnerPost(postId);
        JoinPost joinPost = joinPostReqDto.toEntity(post/*, user*/);
        joinPostRepository.save(joinPost);
        return true;
    }

    public JoinPost readJoinPost(Long id) {
        JoinPost joinPost = joinPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 신청글이 없습니다 id = " + id));
        return joinPost;
    }

    public boolean updateJoinPost(Long id, JoinPostReqDto joinPostReqDto) {
        JoinPost joinPost = joinPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 신청글이 없습니다 id = " + id));
        joinPost.setComment(joinPostReqDto.getComment());
        joinPostRepository.save(joinPost);
        return true;
    }

    public boolean deleteJoinPost(Long id) {
        JoinPost joinPost = joinPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 신청글이 없습니다 id = " + id));
        joinPost.setStatus(Status.DELETED);
        joinPostRepository.save(joinPost);
        return true;
    }
}
