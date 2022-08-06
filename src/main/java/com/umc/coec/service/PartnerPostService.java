package com.umc.coec.service;

import com.umc.coec.config.auth.PrincipalDetails;
import com.umc.coec.domain.enums.Day;
import com.umc.coec.domain.interest.Interest;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.post.PostRepository;
import com.umc.coec.domain.purpose.Purpose;
import com.umc.coec.domain.purpose.PurposeRepository;
import com.umc.coec.domain.skilled.Skilled;
import com.umc.coec.domain.skilled.SkilledRepository;
import com.umc.coec.domain.time.Time;
import com.umc.coec.domain.time.TimeRepository;
import com.umc.coec.dto.partner_post.DayandTime;
import com.umc.coec.dto.partner_post.PostPartnerPostDto;
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

    public List<Post> selectPosts() {
        return postRepository.findPartnerPosts();
    }

    public Skilled selectSkilled(@NotNull Post post) {
        return postRepository.findSkilled(post.getSports().getId(), post.getUser().getId());
    }

    public Post selectPost(Long postId) {
        return postRepository.findPartnerPostById(postId);
    }

    public List<DayandTime> selectDayandTimes(Post post) {
        List<DayandTime> dayandTimes = new ArrayList<>();
        for (int i = 0; i < post.getTimes().size(); i++) {
            Time time = post.getTimes().get(i);
            dayandTimes.add(new DayandTime(time.getDay().toString(), time.getStartTime(), time.getEndTime()));
        }
        return dayandTimes;
    }

    public Boolean selectLikeState(Post post, PrincipalDetails principalDetails) {
        List<Interest> interests = postRepository.findInterestsByPostId(post.getId());
        for (int i = 0; i < interests.size(); i++) {
            Interest interest = interests.get(i);
            if (interest.getUser().getId() == principalDetails.getId())
                return true;
        }
        return false;
    }

    @Transactional
    public Boolean createPost(PostPartnerPostDto postPartnerPostDto/*, Long userId*/) {
        Post post = postPartnerPostDto.toPostEntity();
        //post.getUser().setId(userId);
        postRepository.save(post);

        Skilled skilled = postPartnerPostDto.toSkilledEntity(post.getSports());
        //skilled.getUser().setId(userId);
        skilledRepository.save(skilled);
        List<String> pList = postPartnerPostDto.getPurposes();
        for (int i = 0; i < pList.size(); i++)
            purposeRepository.save(postPartnerPostDto.toPurposeEntity(i, post));
        List<DayandTime> dayandTimeList = postPartnerPostDto.getDayandTimes();
        for (int i = 0; i < dayandTimeList.size(); i++)
            timeRepository.save(postPartnerPostDto.toTimeEntity(i, post));
        return true;
    }
}