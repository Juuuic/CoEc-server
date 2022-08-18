package com.umc.coec.service;

import com.umc.coec.config.auth.PrincipalDetails;
import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.interest.Interest;
import com.umc.coec.domain.interest.InterestRepository;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.post.PostRepository;
import com.umc.coec.domain.time.Time;
import com.umc.coec.domain.time.TimeRepository;
import com.umc.coec.domain.user.User;
import com.umc.coec.dto.mentor.MentoringPostResponseDto;
import com.umc.coec.dto.mentor.MentoringPostsDto;
import com.umc.coec.dto.mentor.MentoringPostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class MentoringService {

    private final PostRepository postrepository;
    private final TimeRepository timeRepository;
    private final InterestRepository interestRepository;

    //멘토멘티 포스트 등록
    @Transactional
    public Boolean saveMentoringPost(MentoringPostsSaveRequestDto requestDto/*, Long id*/) {
        Post newPost = requestDto.toEntity();
        //newPost.getUser().setId(id);

        //추가 : 요일별 시간
        List<Time> times = new ArrayList<>();
        for (int i = 0; i < requestDto.getDayandTimes().size(); i++) {
            Time time = requestDto.toTimeEntity(i, newPost);
            timeRepository.save(time);
            times.add(time);
        }
        newPost.setTimes(times);

        postrepository.save(newPost);
        return true;
    }

    //하트 눌렀는지 확인
    /*
    public Boolean findLikeState(Post post, PrincipalDetails principalDetails) {
        List<Interest> interestList = postrepository.findInterestByPostId(post.getId());
        for (int i =0; i<interestList.size(); i++) {
            Interest interest = interestList.get(i);
            if(interest.getUser().getId() == principalDetails.getId())
                return true;
        }
        return false;
    }
    */

    //멘토멘티 포스트 조회 - 전체 목록
    public List<MentoringPostsDto> selectMentoringPosts(User user) {
        List<Post> postList = postrepository.findMentoringPostsAll();
        List<MentoringPostsDto> postsDtoList = new ArrayList<>();
        for (int i=0; i<postList.size(); i++) {
            MentoringPostsDto m = new MentoringPostsDto(postList.get(i));
            m.setLike(interestRepository.existsByPostAndUser(postList.get(i), user));
            postsDtoList.add(m);
        }
        return postsDtoList;
    }

    //멘토멘티 포스트 조회 - by id
    public MentoringPostResponseDto findMentoringPostById(Long id) {
        Post post = postrepository.findMentoringPostById(id);
        MentoringPostResponseDto MentoringPostDto = new MentoringPostResponseDto(post);

        return MentoringPostDto;
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



    //멘토멘티 포스트 수정
    @Transactional
    public Boolean updateMentoringPost(Long id, MentoringPostsSaveRequestDto requestDto, User user) {
        Post post = postrepository.findMentoringPostById(id);
        Post updatedPost = requestDto.toEntity();

        BeanUtils.copyProperties(updatedPost, post, getNullPropertyNames(updatedPost));
        return true;
    }



    //멘토멘티 포스트 삭제
    public Boolean deleteMentoringPost(Long id) {
        Post post = postrepository.findMentoringPostById(id);

        for(int i=0; i<post.getInterests().size(); i++) {
            post.getInterests().get(i).setStatus(Status.DELETED);
        }
        for(int i=0; i<post.getTimes().size(); i++) {
            post.getTimes().get(i).setStatus(Status.DELETED);
        }

        post.setStatus(Status.DELETED);
        post.getLocation().setStatus(Status.DELETED);
        post.getSports().setStatus(Status.DELETED);


        postrepository.save(post);

        if(post.getStatus().name().equals("DELETED"))
            return true;
        return false;
    }


}
