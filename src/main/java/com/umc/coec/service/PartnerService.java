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
import com.umc.coec.domain.user.User;
import com.umc.coec.domain.user.UserRepository;
import com.umc.coec.dto.partner.PartnerPostReqDto;
import com.umc.coec.dto.partner.PartnerPostResDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Boolean createPost(PartnerPostReqDto partnerPostReqDto, User user) {
        Post post = partnerPostReqDto.toPostEntity(user);

        Skilled skilled = partnerPostReqDto.toSkilledEntity(post.getSports(), user);
        skilledRepository.save(skilled);

        List<Purpose> purposes = new ArrayList<>();
        for (int i = 0; i < partnerPostReqDto.getPurposes().size(); i++) {
            Purpose purpose = partnerPostReqDto.toPurposeEntity(i, post);
            purposeRepository.save(purpose);
            purposes.add(purpose);
        }
        post.setPurposes(purposes);

        List<Time> times = new ArrayList<>();
        for (int i = 0; i < partnerPostReqDto.getDayandTimes().size(); i++) {
            Time time = partnerPostReqDto.toTimeEntity(i, post);
            timeRepository.save(time);
            times.add(time);
        }
        post.setTimes(times);

        postRepository.save(post);
        return true;
    }

    // 파트너 게시물 목록 조회
    public List<PartnerPostResDto> readPartnerPosts() {
        List<Post> posts = postRepository.findPartnerPosts();
        List<PartnerPostResDto> readPostsResDtos = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            PartnerPostResDto pDto = new PartnerPostResDto(posts.get(i));
            readPostsResDtos.add(pDto);
        }
        return readPostsResDtos;
    }

    // 파트너 게시물 조회
    public PartnerPostResDto readPartnerPostByPostId(Long postId, User user) {
        Post post = postRepository.findPartnerPost(postId);
        Skilled skilled = skilledRepository.findBySportsAndUser(post.getSports(), post.getUser());
        PartnerPostResDto partnerPostResDto = new PartnerPostResDto(post, skilled);

        partnerPostResDto.setLikeState(interestRepository.findByPostAndUser(post, user));
        return partnerPostResDto;
    }

    //수정할 때 null인 칼럼 뽑아내기
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    // 파트너 게시물 수정
    @Transactional
    public Boolean updatePost(Long postId, PartnerPostReqDto partnerPostReqDto, User user) {
        Post post = postRepository.findPartnerPost(postId);
        if (post.getUser().equals(user)) {
            post.update(partnerPostReqDto);

            Skilled skilled = skilledRepository.findBySportsAndUser(post.getSports(), user);
            skilled.update(partnerPostReqDto);
            skilledRepository.save(skilled);

            // 기존 목적들 삭제
            for (int i = 0; i < post.getPurposes().size(); i++)
                purposeRepository.delete(post.getPurposes().get(i));
            // 새로운 목적들 추가
            List<Purpose> purposes = new ArrayList<>();
            for (int i = 0; i < partnerPostReqDto.getPurposes().size(); i++) {
                Purpose purpose = partnerPostReqDto.toPurposeEntity(i, post);
                purposeRepository.save(purpose);
                purposes.add(purpose);
            }
            post.setPurposes(purposes);

            // 기존 요일별 시간들 삭제
            for (int i = 0; i < post.getTimes().size(); i++)
                timeRepository.delete(post.getTimes().get(i));
            // 새로운 요일별 시간들 추가
            List<Time> times = new ArrayList<>();
            for (int i = 0; i < partnerPostReqDto.getDayandTimes().size(); i++) {
                Time time = partnerPostReqDto.toTimeEntity(i, post);
                timeRepository.save(time);
                times.add(time);
            }
            post.setTimes(times);

            postRepository.save(post);
            return true;
        }
        return false;
    }

    // 파트너 게시물 삭제
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