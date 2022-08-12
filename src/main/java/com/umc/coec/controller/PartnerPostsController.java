package com.umc.coec.controller;

import com.umc.coec.config.auth.PrincipalDetails;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.skilled.Skilled;
import com.umc.coec.dto.partner_post.*;
import com.umc.coec.service.PartnerPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts/partners")
public class PartnerPostsController {


    private final PartnerPostService partnerPostService;

    /*
    *TODO
    * 전체적인 로직이 컨트롤러 -> 서비스로 이동해야 함
    * 컨트롤러는 서비스로 DTO만 넘기고 서비스에서 모든 처리를 끝낸 후에 DTO만 받아오는 방식으로 변경
    * 컨트롤러에서는 간단한 분기정도만 처리하기
    */

    // 게시물 목록 조회
    @GetMapping("")
    public ResponseEntity<List<ReadPostsResDto>> getPosts(){
        List<Post> posts = partnerPostService.selectPosts();
        List<ReadPostsResDto> readPostsResDtos = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            ReadPostsResDto pDto = new ReadPostsResDto(posts.get(i));
            readPostsResDtos.add(pDto);
        }
        return new ResponseEntity<>(readPostsResDtos, HttpStatus.OK);
    }

    // 게시물 조회
    @GetMapping("/{postId}")
    public ResponseEntity<ReadPostResDto> getPost(@PathVariable("postId") Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Post post = partnerPostService.selectPost(postId);
        Skilled skilled = partnerPostService.selectSkilled(post);
        ReadPostResDto readPostResDto = new ReadPostResDto(post, skilled);
        readPostResDto.setLikeState(partnerPostService.selectLikeState(post, Long.valueOf(3) /*principalDetails.getId()*/));
        return new ResponseEntity<>(readPostResDto, HttpStatus.OK);
    }

    // 게시물 생성
    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody CreatePostReqDto createPostReqDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (partnerPostService.createPost(createPostReqDto/*, principalDetails.getUser()*/))
            return new ResponseEntity<>("등록이 완료되었습니다.", HttpStatus.CREATED);
        return new ResponseEntity<>("등록에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 게시물 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<?> modifyPost(@PathVariable("postId") Long postId, @RequestBody UpdatePostReqDto updatePostReqDto,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            return new ResponseEntity<>(partnerPostService.updatePost(postId, updatePostReqDto, Long.valueOf(3)/*principalDetails.getId()*/), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("수정에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 게시물 삭제
    @PatchMapping("/{postId}/status")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (partnerPostService.deletePost(postId, Long.valueOf(3)/*principalDetails.getId()*/))
            return new ResponseEntity<>("삭제가 완료되었습니다.", HttpStatus.OK);
        return new ResponseEntity<>("삭제에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
