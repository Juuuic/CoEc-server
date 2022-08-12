package com.umc.coec.service;

import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.interest.Interest;
import com.umc.coec.domain.interest.InterestRepository;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.post.PostRepository;
import com.umc.coec.domain.purpose.Purpose;
import com.umc.coec.domain.purpose.PurposeRepository;
import com.umc.coec.domain.skilled.Skilled;
import com.umc.coec.domain.skilled.SkilledRepository;
import com.umc.coec.domain.time.Time;
import com.umc.coec.domain.time.TimeRepository;
import com.umc.coec.dto.partner_post.CreatePostReqDto;
import com.umc.coec.dto.partner_post.ReadPostResDto;
import com.umc.coec.dto.partner_post.UpdatePostReqDto;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PartnerPostService {
    private final Logger logger= LoggerFactory.getLogger(getClass());

    private final PostRepository postRepository;
    private final SkilledRepository skilledRepository;
    private final PurposeRepository purposeRepository;
    private final TimeRepository timeRepository;
    private final InterestRepository interestRepository;

    public List<Post> selectPosts() {
        return postRepository.findPartnerPosts();
    }

    public Skilled selectSkilled(@NotNull Post post) {
        return skilledRepository.findSkilled(post.getSports().getId(), post.getUser().getId());
    }

    public Post selectPost(Long postId) {
        return postRepository.findPartnerPost(postId);
    }

    public Boolean selectLikeState(Post post, Long userId) {
        List<Interest> interests = interestRepository.findInterestsByPostId(post.getId());
        for (int i = 0; i < interests.size(); i++) {
            Interest interest = interests.get(i);
            if (interest.getUser().getId() == userId)
                return true;
        }
        return false;
    }

    @Transactional
    public Boolean createPost(CreatePostReqDto createPostReqDto/*, User user*/) {
        Post post = createPostReqDto.toPostEntity(/*user*/);

        Skilled skilled = createPostReqDto.toSkilledEntity(post.getSports()/*, user*/);
        skilledRepository.save(skilled);

        List<Purpose> purposes = new ArrayList<>();
        for (int i = 0; i < createPostReqDto.getPurposes().size(); i++) {
            Purpose purpose = createPostReqDto.toPurposeEntity(i, post);
            purposeRepository.save(purpose);
            purposes.add(purpose);
        }
        post.setPurposes(purposes);

        List<Time> times = new ArrayList<>();
        for (int i = 0; i < createPostReqDto.getDayandTimes().size(); i++) {
            Time time = createPostReqDto.toTimeEntity(i, post);
            timeRepository.save(time);
            times.add(time);
        }
        post.setTimes(times);

        postRepository.save(post);
        return true;
    }

    @Transactional
    public ReadPostResDto updatePost(Long postId, UpdatePostReqDto updatePostReqDto, Long userId) {
        Post post = postRepository.findPartnerPost(postId);
        if (post.getUser().getId() == userId) {
            post.update(updatePostReqDto);

            Skilled skilled = skilledRepository.findSkilled(post.getSports().getId(), userId);
            skilled.update(updatePostReqDto);
            skilledRepository.save(skilled);

            // 기존 목적들 삭제
            for (int i = 0; i < post.getPurposes().size(); i++)
                purposeRepository.delete(post.getPurposes().get(i));
            // 새로운 목적들 추가
            List<Purpose> purposes = new ArrayList<>();
            for (int i = 0; i < updatePostReqDto.getPurposes().size(); i++) {
                Purpose purpose = updatePostReqDto.toPurposeEntity(i, post);
                purposeRepository.save(purpose);
                purposes.add(purpose);
            }
            post.setPurposes(purposes);

            // 기존 요일별 시간들 삭제
            for (int i = 0; i < post.getTimes().size(); i++)
                timeRepository.delete(post.getTimes().get(i));
            // 새로운 요일별 시간들 추가
            List<Time> times = new ArrayList<>();
            for (int i = 0; i < updatePostReqDto.getDayandTimes().size(); i++) {
                Time time = updatePostReqDto.toTimeEntity(i, post);
                timeRepository.save(time);
                times.add(time);
            }
            post.setTimes(times);

            postRepository.save(post);

            // 게시물 조회 Dto로 변환
            ReadPostResDto readPostResDto = new ReadPostResDto(post, skilled);
            readPostResDto.setLikeState(this.selectLikeState(post, userId));
            return readPostResDto;
        }
        return null;
    }

    public Boolean deletePost(Long postId, Long userId) {
        Post post = postRepository.findPartnerPost(postId);
        if (post.getUser().getId() == userId) {
            post.setStatus(Status.DELETED);

            post.getSports().setStatus(Status.DELETED);
            post.getLocation().setStatus(Status.DELETED);

            for (int i = 0; i < post.getJoinPosts().size(); i++)
                post.getJoinPosts().get(i).setStatus(Status.DELETED);
            for (int i = 0; i < post.getPurposes().size(); i++)
                post.getPurposes().get(i).setStatus(Status.DELETED);
            for (int i = 0; i < post.getTimes().size(); i++)
                post.getTimes().get(i).setStatus(Status.DELETED);
            for (int i = 0; i < post.getInterests().size(); i++)
                post.getInterests().get(i).setStatus(Status.DELETED);
            postRepository.save(post);
            return true;
        }
        return false;
    }
}