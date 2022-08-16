package com.umc.coec.service;

import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.interest.InterestRepository;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.post.PostRepository;
import com.umc.coec.domain.purpose.Purpose;
import com.umc.coec.domain.purpose.PurposeRepository;
import com.umc.coec.domain.skilled.Skilled;
import com.umc.coec.domain.skilled.SkilledRepository;
import com.umc.coec.domain.time.Time;
import com.umc.coec.domain.time.TimeRepository;
import com.umc.coec.domain.user.User;
import com.umc.coec.dto.partner.PartnerPostRequestDto;
import com.umc.coec.dto.partner.PartnerPostResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PartnerService {
    private final Logger logger= LoggerFactory.getLogger(getClass());

    private final PostRepository postRepository;
    private final SkilledRepository skilledRepository;
    private final PurposeRepository purposeRepository;
    private final TimeRepository timeRepository;
    private final InterestRepository interestRepository;

    // 파트너 게시물 등록
    @Transactional
    public Boolean createPartnerPost(PartnerPostRequestDto partnerPostRequestDto, User user) {
        Post post = partnerPostRequestDto.toPostEntity(user);

        Skilled skilled = partnerPostRequestDto.toSkilledEntity(post.getSports(), user);
        skilledRepository.save(skilled);

        List<Purpose> purposes = new ArrayList<>();
        for (int i = 0; i < partnerPostRequestDto.getPurposes().size(); i++) {
            Purpose purpose = partnerPostRequestDto.toPurposeEntity(i, post);
            purposeRepository.save(purpose);
            purposes.add(purpose);
        }
        post.setPurposes(purposes);

        List<Time> times = new ArrayList<>();
        for (int i = 0; i < partnerPostRequestDto.getDayandTimes().size(); i++) {
            Time time = partnerPostRequestDto.toTimeEntity(i, post);
            timeRepository.save(time);
            times.add(time);
        }
        post.setTimes(times);

        postRepository.save(post);
        return true;
    }

    // 파트너 게시물 목록 조회
    public List<PartnerPostResponseDto> readPartnerPosts() {
        List<Post> posts = postRepository.findPartnerPosts();
        List<PartnerPostResponseDto> readPostsResDtos = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            PartnerPostResponseDto pDto = new PartnerPostResponseDto(posts.get(i));
            readPostsResDtos.add(pDto);
        }
        return readPostsResDtos;
    }

    // 파트너 게시물 조회
    public PartnerPostResponseDto readPartnerPostByPostId(Long postId, User user) {
        Post post = postRepository.findPartnerPost(postId);
        Skilled skilled = skilledRepository.findBySportsAndUser(post.getSports(), post.getUser());
        PartnerPostResponseDto partnerPostResponseDto = new PartnerPostResponseDto(post, skilled);

        partnerPostResponseDto.setLikeState(interestRepository.existsByPostAndUser(post, user));
        return partnerPostResponseDto;
    }

    // 파트너 게시물 수정
    @Transactional
    public Boolean updatePartnerPost(Long postId, PartnerPostRequestDto partnerPostRequestDto, User user) {
        Post post = postRepository.findPartnerPost(postId);
        if (post.getUser().equals(user)) {
            post.update(partnerPostRequestDto);

            Skilled skilled = skilledRepository.findBySportsAndUser(post.getSports(), user);
            skilled.update(partnerPostRequestDto);
            skilledRepository.save(skilled);

            // 기존 목적들 삭제
            for (int i = 0; i < post.getPurposes().size(); i++)
                purposeRepository.delete(post.getPurposes().get(i));
            // 새로운 목적들 추가
            List<Purpose> purposes = new ArrayList<>();
            for (int i = 0; i < partnerPostRequestDto.getPurposes().size(); i++) {
                Purpose purpose = partnerPostRequestDto.toPurposeEntity(i, post);
                purposeRepository.save(purpose);
                purposes.add(purpose);
            }
            post.setPurposes(purposes);

            // 기존 요일별 시간들 삭제
            for (int i = 0; i < post.getTimes().size(); i++)
                timeRepository.delete(post.getTimes().get(i));
            // 새로운 요일별 시간들 추가
            List<Time> times = new ArrayList<>();
            for (int i = 0; i < partnerPostRequestDto.getDayandTimes().size(); i++) {
                Time time = partnerPostRequestDto.toTimeEntity(i, post);
                timeRepository.save(time);
                times.add(time);
            }
            post.setTimes(times);

            postRepository.save(post);

            // getNullPropertyNames() 사용 ver 수정 중 (밑 코드 = 미완 상태)
            // -> BeanUtils.copyProperties() 사용하면, DB에 기존에 있던 게 수정되는 게 아니라 새로 추가되는 문제점 발생
            // 일단은 각 update()에서 필수 입력 값들에 null 넣으면 기존 거 그대로 가는 방향으로 함
            /*
            Post updatedPost = partnerPostRequestDto.toPostEntity(user);
            Sports updatedSports = updatedPost.getSports();
            Location updatedLocation = updatedPost.getLocation();

            // 모집중 or 모집완료에 따라 status 변경
            updatedSports.setStatus(updatedPost.getStatus().equals("모집중") ? Status.ACTIVE : Status.INACTIVE);
            updatedLocation.setStatus(updatedPost.getStatus().equals("모집중") ? Status.ACTIVE : Status.INACTIVE);
            updatedPost.setJoinPosts(post.getJoinPosts());
            for (int i = 0; i < post.getJoinPosts().size(); i++)
                updatedPost.getJoinPosts().get(i).setStatus(updatedPost.getStatus().equals("모집중") ? Status.ACTIVE : Status.INACTIVE);
            updatedPost.setInterests(post.getInterests());
            // 일단 모집완료된 글에는 관심 못 누르게 설정
            for (int i = 0; i < post.getInterests().size(); i++)
                updatedPost.getInterests().get(i).setStatus(updatedPost.getStatus().equals("모집중") ? Status.ACTIVE : Status.INACTIVE);

            Skilled skilled = skilledRepository.findBySportsAndUser(post.getSports(), user);
            Skilled updatedSkilled = partnerPostRequestDto.toSkilledEntity(updatedPost.getSports(), user);

            // 기존 목적들 삭제
            for (int i = 0; i < post.getPurposes().size(); i++)
                purposeRepository.delete(post.getPurposes().get(i));
            // 기존 요일별 시간들 삭제
            for (int i = 0; i < post.getTimes().size(); i++)
                timeRepository.delete(post.getTimes().get(i));

            List<Purpose> purposes = new ArrayList<>();
            // 새로운 목적들 추가
            for (int i = 0; i < partnerPostRequestDto.getPurposes().size(); i++) {
                Purpose purpose = partnerPostRequestDto.toPurposeEntity(i, updatedPost);
                purposeRepository.save(purpose);
                purposes.add(purpose);
            }
            updatedPost.setPurposes(purposes);

            // 새로운 요일별 시간들 추가
            List<Time> times = new ArrayList<>();
            for (int i = 0; i < partnerPostRequestDto.getDayandTimes().size(); i++) {
                Time time = partnerPostRequestDto.toTimeEntity(i, updatedPost);
                timeRepository.save(time);
                times.add(time);
            }
            updatedPost.setTimes(times);
            BeanUtils.copyProperties(updatedSports, post.getSports(), getNullPropertyNames(updatedSports));
            BeanUtils.copyProperties(updatedLocation, post.getLocation(), getNullPropertyNames(updatedLocation));
            BeanUtils.copyProperties(updatedPost, post, getNullPropertyNames(updatedPost));
            BeanUtils.copyProperties(updatedSkilled, skilled, getNullPropertyNames(updatedSkilled));
            */

            return true;
        }
        return false;
    }

    // 파트너 게시물 삭제
    public Boolean deletePartnerPost(Long postId, Long userId) {
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