package com.umc.coec.service;

import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.post.PostRepository;
import com.umc.coec.domain.skilled.Skilled;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PartnerPostService {
    private final Logger logger= LoggerFactory.getLogger(getClass());

    private final PostRepository postRepository;

    public List<Post> selectPosts() {
        return postRepository.findPartnerPosts();
    }

    public Skilled getSkilled(Post post) {
        return postRepository.findSkilled(post.getSports().getId(), post.getUser().getId());
    }
}