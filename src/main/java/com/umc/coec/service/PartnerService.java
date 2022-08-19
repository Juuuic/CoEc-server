package com.umc.coec.service;

import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.interest.InterestRepository;
import com.umc.coec.domain.location.Location;
import com.umc.coec.domain.location.LocationRepository;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.post.PostRepository;
import com.umc.coec.domain.purpose.Purpose;
import com.umc.coec.domain.purpose.PurposeRepository;
import com.umc.coec.domain.skilled.Skilled;
import com.umc.coec.domain.skilled.SkilledRepository;
import com.umc.coec.domain.sports.Sports;
import com.umc.coec.domain.sports.SportsRepository;
import com.umc.coec.domain.time.Time;
import com.umc.coec.domain.time.TimeRepository;
import com.umc.coec.domain.user.User;
import com.umc.coec.dto.partner.PartnerPostRequestDto;
import com.umc.coec.dto.partner.PartnerPostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.RuntimeErrorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PartnerService {

    private final PostRepository postRepository;
    private final SkilledRepository skilledRepository;
    private final PurposeRepository purposeRepository;
    private final TimeRepository timeRepository;
    private final InterestRepository interestRepository;
    private final SportsRepository sportsRepository;
    private final LocationRepository locationRepository;

    // 파트너 게시물 등록
    @Transactional
    public Boolean createPartnerPost(PartnerPostRequestDto partnerPostRequestDto, User user) {

        //기존에 존재하는 스포츠 가져오기
        Sports selectedSports = sportsRepository.findByName(partnerPostRequestDto.getSportsName())
                    .orElseThrow(()->{
                        throw new RuntimeException("해당하는 스포츠가 존재하지 않습니다.");
                    });

        //지역 생성
        Location location = Location.builder()
                    .siDo(partnerPostRequestDto.getSiDo())
                    .siGunGu(partnerPostRequestDto.getSiGunGu())
                    .eupMyunDongLi(partnerPostRequestDto.getEupMyunDongLi())
                    .status(Status.ACTIVE)
                    .build();

        Skilled skilled = partnerPostRequestDto.toSkilledEntity(selectedSports, user);
        skilledRepository.save(skilled);

        Post post = partnerPostRequestDto.toPostEntity(user,selectedSports,location);
        postRepository.save(post);


        List<Purpose> purposes = new ArrayList<>();

        for (int i = 0; i < partnerPostRequestDto.getPurposes().size(); i++) {
            Purpose purpose = partnerPostRequestDto.toPurposeEntity(i, post);
            purposeRepository.save(purpose);
            purposes.add(purpose);
        }

        List<Time> times = new ArrayList<>();
        for (int i = 0; i < partnerPostRequestDto.getDayandTimes().size(); i++) {
            Time time = partnerPostRequestDto.toTimeEntity(i, post);
            timeRepository.save(time);
            times.add(time);
        }
        post.setTimes(times);

        return true;
    }

    // 파트너 게시물 목록 조회
    public List<PartnerPostResponseDto> getPartnerPosts() {
        List<Post> posts = postRepository.findPartnerPosts();
        List<PartnerPostResponseDto> postResDtos = new ArrayList<>();
        posts.forEach(post -> postResDtos.add(new PartnerPostResponseDto(post)));
        return postResDtos;
    }

    // 파트너 게시물 조회
    public PartnerPostResponseDto getPartnerPostByPostId(Long postId, User user) {
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