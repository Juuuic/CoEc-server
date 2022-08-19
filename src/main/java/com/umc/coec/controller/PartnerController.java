package com.umc.coec.controller;

import com.umc.coec.config.auth.PrincipalDetails;
import com.umc.coec.dto.partner.*;
import com.umc.coec.service.PartnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@RestController
public class PartnerController {

    private final PartnerService partnerService;

    // 파트너 게시물 목록 조회
    @GetMapping("/partners")
    public ResponseEntity<List<PartnerPostResponseDto>> getPartnerPosts() {
        return new ResponseEntity<>(partnerService.getPartnerPosts(), HttpStatus.OK);
    }

    // 파트너 게시물 생성
    @PostMapping("/partners")
    public ResponseEntity<?> createPartnerPost(
                @RequestBody PartnerPostRequestDto partnerPostRequestDto,
                @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (partnerService.createPartnerPost(partnerPostRequestDto, principalDetails.getUser()))
            return new ResponseEntity<>("등록이 완료되었습니다.", HttpStatus.CREATED);
        return new ResponseEntity<>("등록에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // 파트너 게시물 조회
    @GetMapping("/partners/{postId}")
    public ResponseEntity<?> getPartnerPostByPostId(
                @PathVariable("postId") Long postId,
                @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return new ResponseEntity<>(partnerService.getPartnerPostByPostId(postId, principalDetails.getUser()), HttpStatus.OK);
    }

    // 파트너 게시물 수정
    @PutMapping("/partners/{postId}")
    public ResponseEntity<?> updatePartnerPost(
                @PathVariable("postId") Long postId,
                @RequestBody PartnerPostRequestDto partnerPostRequestDto,
                @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (partnerService.updatePartnerPost(postId, partnerPostRequestDto, principalDetails.getUser()))
            return new ResponseEntity<>("수정이 완료되었습니다.", HttpStatus.CREATED);
        return new ResponseEntity<>("수정에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 파트너 게시물 삭제
    @PatchMapping("/partners/{postId}/status")
    public ResponseEntity<?> deletePartnerPost(
                @PathVariable("postId") Long postId,
                @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (partnerService.deletePartnerPost(postId, principalDetails.getId()))
            return new ResponseEntity<>("삭제가 완료되었습니다.", HttpStatus.NO_CONTENT);
        return new ResponseEntity<>("삭제에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
