package com.umc.coec.service;

import com.umc.coec.config.auth.PrincipalDetails;
import com.umc.coec.domain.enums.Status;
import com.umc.coec.domain.interest.Interest;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.post.PostRepository;
import com.umc.coec.dto.mentor.MentoringPostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class MentoringService {

    private final PostRepository postrepository;

    //멘토멘티 포스트 등록
    @Transactional
    public Boolean saveMentoringPost(MentoringPostsSaveRequestDto requestDto/*, Long id*/) {
        Post newPost = requestDto.toEntity();
        //newPost.getUser().setId(id);
        postrepository.save(newPost);
        return true;
    }

    //하트 눌렀는지 확인
    public Boolean findLikeState(Post post, PrincipalDetails principalDetails) {
        List<Interest> interestList = postrepository.findInterestByPostId(post.getId());
        for (int i =0; i<interestList.size(); i++) {
            Interest interest = interestList.get(i);
            if(interest.getUser().getId() == principalDetails.getId())
                return true;
        }
        return false;
    }

    //멘토멘티 포스트 조회 - 전체 목록
    public List<Post> selectMentoringPosts() {
        return postrepository.findMentoringPostsAll();
    }

    //멘토멘티 포스트 조회 - by id
    public Post findMentoringPostById(Long id) {
        return postrepository.findMentoringPostById(id);
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
    public Boolean updateMentoringPost(Long id, MentoringPostsSaveRequestDto requestDto) {
        Post post = postrepository.findMentoringPostById(id);
        Post updatedPost = requestDto.toEntity();


        BeanUtils.copyProperties(updatedPost, post, getNullPropertyNames(updatedPost));
        return true;
    }



    //멘토멘티 포스트 삭제
    public Boolean deleteMentoringPost(Long id) {
        Post post = findMentoringPostById(id);
        post.setStatus(Status.DELETED);
        postrepository.save(post);

        if(post.getStatus().name().equals("DELETED"))
            return true;
        return false;
    }


}
