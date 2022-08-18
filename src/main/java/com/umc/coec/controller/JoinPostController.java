package com.umc.coec.controller;

import com.umc.coec.config.auth.PrincipalDetails;
import com.umc.coec.dto.join_post.JoinPostRequestDto;
import com.umc.coec.dto.join_post.JoinPostResponseDto;
import com.umc.coec.service.JoinPostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class JoinPostController {
    private final Logger logger= LoggerFactory.getLogger(getClass());
    private final JoinPostService joinPostService;

    @PostMapping("/api/v1/posts/join/{postId}")
    public ResponseEntity<?> createJoinPost(@PathVariable("postId") Long postId, @RequestBody JoinPostRequestDto joinPostRequestDto
            , @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (joinPostService.createJoinPost(postId, joinPostRequestDto, principalDetails.getUser()))
            return new ResponseEntity<>("신청이 완료되었습니다.", HttpStatus.CREATED);
        return new ResponseEntity<>("신청에 실패하였습니다.",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/api/v1/posts/join/{joinPostId}")
    public ResponseEntity<?> readJoinPost(@PathVariable("joinPostId") Long joinPostId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        JoinPostResponseDto joinPostResponseDto = new JoinPostResponseDto(joinPostService.readJoinPost(joinPostId, principalDetails.getUser()));
        return new ResponseEntity<>(joinPostResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/api/v1/posts/join/{joinPostId}")
    public ResponseEntity<?> updateJoinPost(@PathVariable("joinPostId") Long joinPostId, @RequestBody JoinPostRequestDto joinPostRequestDto
            , @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (joinPostService.updateJoinPost(joinPostId, joinPostRequestDto, principalDetails.getUser()))
            return new ResponseEntity<>("신청글 수정이 완료되었습니다.", HttpStatus.CREATED);
        return new ResponseEntity<>("신청글 수정에 실패하였습니다.",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping("/api/v1/posts/join/{joinPostId}/status")
    public ResponseEntity<?> deleteJoinPost(@PathVariable("joinPostId") Long joinPostId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (joinPostService.deleteJoinPost(joinPostId, principalDetails.getUser()))
            return new ResponseEntity<>("신청글 삭제가 완료되었습니다.", HttpStatus.OK);
        return new ResponseEntity<>("신청글 삭제에 실패하였습니다.",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
