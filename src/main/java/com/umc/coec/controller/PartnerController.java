package com.umc.coec.controller;

import com.umc.coec.config.auth.PrincipalDetails;
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
public class PartnerController {

    private final Logger logger= LoggerFactory.getLogger(getClass());

    private final PartnerService partnerService;

    // 파트너 게시물 생성
    @PostMapping("/api/v1/posts/partners")
    public ResponseEntity<?> createPartnerPost(@RequestBody PartnerPostRequestDto partnerPostRequestDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (partnerService.createPartnerPost(partnerPostRequestDto, principalDetails.getUser()))
            return new ResponseEntity<>("등록이 완료되었습니다.", HttpStatus.CREATED);
        return new ResponseEntity<>("등록에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 파트너 게시물 목록 조회
    @GetMapping("/api/v1/posts/partners")
    public ResponseEntity<List<PartnerPostResponseDto>> readPartnerPosts() {
        return new ResponseEntity<>(partnerService.readPartnerPosts(), HttpStatus.OK);
    }

    // 파트너 게시물 조회
    @GetMapping("/api/v1/posts/partners/{postId}")
    public ResponseEntity<PartnerPostResponseDto> readPartnerPostByPostId(@PathVariable("postId") Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return new ResponseEntity<>(partnerService.readPartnerPostByPostId(postId, principalDetails.getUser()), HttpStatus.OK);
    }

    // 파트너 게시물 수정
    @PutMapping("/api/v1/posts/partners/{postId}")
    public ResponseEntity<?> updatePartnerPost(@PathVariable("postId") Long postId, @RequestBody PartnerPostRequestDto partnerPostRequestDto,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (partnerService.updatePartnerPost(postId, partnerPostRequestDto, principalDetails.getUser()))
            return new ResponseEntity<>("수정이 완료되었습니다.", HttpStatus.CREATED);
        return new ResponseEntity<>("수정에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 파트너 게시물 삭제
    @PatchMapping("/api/v1/posts/partners/{postId}/status")
    public ResponseEntity<?> deletePartnerPost(@PathVariable("postId") Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (partnerService.deletePartnerPost(postId, principalDetails.getId()))
            return new ResponseEntity<>("삭제가 완료되었습니다.", HttpStatus.OK);
        return new ResponseEntity<>("삭제에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
