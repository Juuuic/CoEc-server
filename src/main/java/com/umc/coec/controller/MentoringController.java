package com.umc.coec.controller;

import com.umc.coec.config.auth.PrincipalDetails;
import com.umc.coec.domain.post.Post;
import com.umc.coec.dto.mentor.MentoringPostResponseDto;
import com.umc.coec.dto.mentor.MentoringPostsDto;
import com.umc.coec.dto.mentor.MentoringPostsSaveRequestDto;
import com.umc.coec.service.MentoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MentoringController {

    private final MentoringService mentoringService;

    //1. 멘토멘티 포스트 등록
    @PostMapping("/api/v1/posts/mentoring")
    public ResponseEntity<?> saveMentoringPost(@RequestBody MentoringPostsSaveRequestDto requestDto) {
        if(mentoringService.saveMentoringPost(requestDto))
            return new ResponseEntity<>("등록 완료되었습니다", HttpStatus.CREATED);
        return new ResponseEntity<>("등록 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    //2. 멘토멘티 포스트 조회 - 전체 목록
    @GetMapping("/api/v1/posts/mentoring")
    public ResponseEntity<List<MentoringPostsDto>> getMentoringPosts(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<Post> postList = mentoringService.selectMentoringPosts();
        List<MentoringPostsDto> postsDtoList = new ArrayList<>();
        for (int i=0; i<postList.size(); i++) {
            MentoringPostsDto m = new MentoringPostsDto(postList.get(i));
            m.setLike(mentoringService.findLikeState(postList.get(i), principalDetails));
            postsDtoList.add(m);
        }
        return new ResponseEntity<>(postsDtoList, HttpStatus.OK);
    }



    //3. 멘토멘티 포스트 조회 - 포스트별 상세 조회
    @GetMapping("/api/v1/posts/mentoring/{id}")
    public ResponseEntity<MentoringPostResponseDto> getMentoringPostById(@PathVariable("id") Long id) {
        Post post = mentoringService.findMentoringPostById(id);
        MentoringPostResponseDto responsePost = new MentoringPostResponseDto(post);

        return new ResponseEntity<>(responsePost, HttpStatus.OK);
    }


    //4. 멘토멘티 포스트 수정
    @PutMapping("/api/v1/posts/mentoring/{id}")
    public ResponseEntity<?> updateMentoringPost(@PathVariable("id") Long id, @RequestBody MentoringPostsSaveRequestDto requestDto) {
        if(mentoringService.updateMentoringPost(id, requestDto))
            return new ResponseEntity<>("수정 완료되었습니다", HttpStatus.OK);
        return new ResponseEntity<>("수정 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    //5. 멘토멘티 포스트 삭제
    @DeleteMapping("/api/v1/posts/mentoring/{id}/status")
    public ResponseEntity<?> dropMentoringPost(@PathVariable("id") Long id) {
        if (mentoringService.deleteMentoringPost(id))
            return new ResponseEntity<>("삭제 완료되었습니다", HttpStatus.OK);
        return new ResponseEntity<>("삭제 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
