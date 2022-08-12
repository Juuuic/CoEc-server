package com.umc.coec.controller;

import com.umc.coec.config.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

      @GetMapping("/user")
      public ResponseEntity<?> userController(@AuthenticationPrincipal PrincipalDetails principalDetails){
            log.info("principal : {}",principalDetails);
            return new ResponseEntity<>("유저 컨트롤러",HttpStatus.OK);
      }
      @GetMapping("/admin")
      public ResponseEntity<?> adminController(){
            return new ResponseEntity<>("어드민 컨트롤러",HttpStatus.OK);
      }
}
