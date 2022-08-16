package com.umc.coec.service;

import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.join_post.JoinPost;
import com.umc.coec.domain.join_post.JoinPostRepository;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.post.PostRepository;
import com.umc.coec.domain.user.User;
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

    public boolean createJoinPost(Long postId, JoinPostReqDto joinPostReqDto, User user) {
        Post post = postRepository.findJoinablePost(postId);
        if (post == null)
            return false;
        if (!post.getUser().equals(user)) {
            JoinPost joinPost = joinPostReqDto.toEntity(post, user);
            joinPostRepository.save(joinPost);
            return true;
        }
        // 게시자는 자신의 게시물에 신청 못 하게끔
        new IllegalArgumentException("자신의 게시물에 신청할 수 없습니다.");
        return false;
    }

    public JoinPost readJoinPost(Long id, User user) {
        JoinPost joinPost = joinPostRepository.findByIdAndUser(id, user);
        if (joinPost == null)
            new IllegalArgumentException("신청글 조회에 실패하였습니다.");
        return joinPost;
    }

    public boolean updateJoinPost(Long id, JoinPostReqDto joinPostReqDto, User user) {
        JoinPost joinPost = joinPostRepository.findByIdAndUser(id, user);
        if (joinPost == null)
            return false;
        joinPost.setComment(joinPostReqDto.getComment());
        joinPostRepository.save(joinPost);
        return true;
    }

    public boolean deleteJoinPost(Long id, User user) {
        JoinPost joinPost = joinPostRepository.findByIdAndUser(id, user);
        if (joinPost == null)
            return false;
        joinPost.setStatus(Status.DELETED);
        joinPostRepository.save(joinPost);
        return true;
    }
}
