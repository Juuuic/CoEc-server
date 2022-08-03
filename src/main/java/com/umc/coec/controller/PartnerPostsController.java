package com.umc.coec.controller;

import com.umc.coec.config.auth.PrincipalDetails;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.skilled.Skilled;
import com.umc.coec.domain.user.User;
import com.umc.coec.dto.partner_post.DayandTime;
import com.umc.coec.dto.partner_post.GetPartnerPostDto;
import com.umc.coec.dto.partner_post.GetPartnerPostsDto;
import com.umc.coec.dto.partner_post.PostPartnerPostDto;
import com.umc.coec.service.PartnerPostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
            Skilled skilled = partnerPostService.selectSkilled(posts.get(i));
            GetPartnerPostsDto pDto = new GetPartnerPostsDto(posts.get(i), skilled);
            getPartnerPostsDtos.add(pDto);
        }
        return new ResponseEntity<>(getPartnerPostsDtos, HttpStatus.OK);
    }

    // 게시물 조회
    @GetMapping("/{postId}")
    public ResponseEntity<GetPartnerPostDto> getPost(@PathVariable("postId") Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Post post = partnerPostService.selectPost(postId);
        Skilled skilled = partnerPostService.selectSkilled(post);
        GetPartnerPostDto getPartnerPostDto = new GetPartnerPostDto(post, skilled);
        getPartnerPostDto.setDayandTimes(partnerPostService.selectDayandTimes(post));
        getPartnerPostDto.setLikeState(partnerPostService.selectLikeState(post, principalDetails));
        return new ResponseEntity<>(getPartnerPostDto, HttpStatus.OK);
    }

    // 게시물 생성
    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody PostPartnerPostDto postPartnerPostDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (partnerPostService.createPost(postPartnerPostDto/*, principalDetails.getId()*/))
            return new ResponseEntity<>("등록이 완료되었습니다.", HttpStatus.CREATED);
        return new ResponseEntity<>("등록에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
