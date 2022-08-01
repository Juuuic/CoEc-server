package com.umc.coec.controller;

import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.skilled.Skilled;
import com.umc.coec.dto.partner_post.GetPartnerPostsDto;
import com.umc.coec.service.PartnerPostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts/partners")
public class PartnerPostsController {

    private final Logger logger= LoggerFactory.getLogger(getClass());

    private final PartnerPostService partnerPostService;

    // 게시물 목록 조회
    @GetMapping("")
    public ResponseEntity<List<GetPartnerPostsDto>> getPosts(){
        List<Post> posts = partnerPostService.selectPosts();
        List<GetPartnerPostsDto> getPartnerPostsDtos = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            Skilled skilled = partnerPostService.getSkilled(posts.get(i));
            GetPartnerPostsDto pDto = new GetPartnerPostsDto(posts.get(i), skilled);
            getPartnerPostsDtos.add(pDto);
        }
        return new ResponseEntity<>(getPartnerPostsDtos, HttpStatus.OK);
    }
}
