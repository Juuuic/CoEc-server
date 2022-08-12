package com.umc.coec.controller;

import com.umc.coec.config.auth.PrincipalDetails;
import com.umc.coec.domain.post.Post;
import com.umc.coec.domain.skilled.Skilled;
import com.umc.coec.domain.user.User;
import com.umc.coec.domain.user.UserRepository;
import com.umc.coec.dto.partner.*;
import com.umc.coec.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts/partners")
public class PartnerController {

    private final Logger logger= LoggerFactory.getLogger(getClass());

    private final PartnerService partnerService;
    private final UserRepository userRepository;

    // 파트너 게시물 생성
    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody PartnerPostReqDto partnerPostReqDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 로그인한 User 대신 테스트용 User
        User user = userRepository.findById(Long.valueOf(3))
                .orElseThrow(() -> new IllegalArgumentException());
        if (partnerService.createPost(partnerPostReqDto, user/*, principalDetails.getUser()*/))
            return new ResponseEntity<>("등록이 완료되었습니다.", HttpStatus.CREATED);
        return new ResponseEntity<>("등록에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 파트너 게시물 목록 조회
    @GetMapping("")
    public ResponseEntity<List<PartnerPostResDto>> readPartnerPosts() {
        return new ResponseEntity<>(partnerService.readPartnerPosts(), HttpStatus.OK);
    }

    // 파트너 게시물 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PartnerPostResDto> readPartnerPostByPostId(@PathVariable("postId") Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 로그인한 User 대신 테스트용 User
        User user = userRepository.findById(Long.valueOf(3))
                .orElseThrow(() -> new IllegalArgumentException());
        return new ResponseEntity<>(partnerService.readPartnerPostByPostId(postId, user/*, principalDetails.getUser()*/), HttpStatus.OK);
    }

    // 파트너 게시물 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<?> updatePartnerPost(@PathVariable("postId") Long postId, @RequestBody PartnerPostReqDto partnerPostReqDto,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 로그인한 User 대신 테스트용 User
        User user = userRepository.findById(Long.valueOf(3))
                .orElseThrow(() -> new IllegalArgumentException());
        if (partnerService.updatePost(postId, partnerPostReqDto, user/*principalDetails.getUser()*/))
            return new ResponseEntity<>("수정이 완료되었습니다.", HttpStatus.CREATED);
        return new ResponseEntity<>("수정에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 파트너 게시물 삭제
    @PatchMapping("/{postId}/status")
    public ResponseEntity<?> deletePartnerPost(@PathVariable("postId") Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (partnerService.deletePost(postId, Long.valueOf(3)/*principalDetails.getId()*/))
            return new ResponseEntity<>("삭제가 완료되었습니다.", HttpStatus.OK);
        return new ResponseEntity<>("삭제에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
