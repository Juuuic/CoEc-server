package com.umc.coec.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

      @GetMapping("/user")
      public ResponseEntity<?> userController(){
            return new ResponseEntity<>("유저 컨트롤러",HttpStatus.OK);
      }
      @GetMapping("/admin")
      public ResponseEntity<?> adminController(){
            return new ResponseEntity<>("어드민 컨트롤러",HttpStatus.OK);
      }
}
