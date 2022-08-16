package com.umc.coec.controller;

import com.umc.coec.config.auth.PrincipalDetails;
import com.umc.coec.dto.join_post.JoinPostReqDto;
import com.umc.coec.dto.join_post.JoinPostResDto;
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
@RequestMapping("/api/v1/posts/join")
public class JoinPostController {
    private final Logger logger= LoggerFactory.getLogger(getClass());
    private final JoinPostService joinPostService;

    @PostMapping("/{postId}")
    public ResponseEntity<?> createJoinPost(@PathVariable("postId") Long postId, @RequestBody JoinPostReqDto joinPostReqDto
            , @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (joinPostService.createJoinPost(postId, joinPostReqDto, principalDetails.getUser()))
            return new ResponseEntity<>("신청이 완료되었습니다.", HttpStatus.CREATED);
        return new ResponseEntity<>("신청에 실패하였습니다.",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{joinPostId}")
    public ResponseEntity<?> readJoinPost(@PathVariable("joinPostId") Long joinPostId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        JoinPostResDto joinPostResDto = new JoinPostResDto(joinPostService.readJoinPost(joinPostId, principalDetails.getUser()));
        return new ResponseEntity<>(joinPostResDto, HttpStatus.OK);
    }

    @PatchMapping("/{joinPostId}")
    public ResponseEntity<?> updateJoinPost(@PathVariable("joinPostId") Long joinPostId, @RequestBody JoinPostReqDto joinPostReqDto
            , @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (joinPostService.updateJoinPost(joinPostId, joinPostReqDto, principalDetails.getUser()))
            return new ResponseEntity<>("신청글 수정이 완료되었습니다.", HttpStatus.CREATED);
        return new ResponseEntity<>("신청글 수정에 실패하였습니다.",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping("/{joinPostId}/status")
    public ResponseEntity<?> deleteJoinPost(@PathVariable("joinPostId") Long joinPostId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (joinPostService.deleteJoinPost(joinPostId, principalDetails.getUser()))
            return new ResponseEntity<>("신청글 삭제가 완료되었습니다.", HttpStatus.OK);
        return new ResponseEntity<>("신청글 삭제에 실패하였습니다.",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
