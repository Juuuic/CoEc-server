package com.umc.coec.service;

import com.umc.coec.config.auth.PrincipalDetails;
import com.umc.coec.domain.interest.Interest;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.post.PostRepository;
import com.umc.coec.domain.skilled.Skilled;
import com.umc.coec.domain.time.Time;
import com.umc.coec.domain.user.User;
import com.umc.coec.dto.partner_post.DayandTime;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PartnerPostService {
    private final Logger logger= LoggerFactory.getLogger(getClass());

    private final PostRepository postRepository;

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
            dayandTimes.add(new DayandTime(time.getDay(), time.getStartTime(), time.getEndTime()));
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
}