package com.example.bookmark.domain.auth.controller;

import com.example.bookmark.domain.auth.dto.LoginResponse;
import com.example.bookmark.domain.auth.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    // 1) 카카오 로그인 URL을 받아가는 엔드포인트
    @GetMapping("/login-url")
    public ResponseEntity<String> getLoginUrl() {
        String url = kakaoAuthService.generateLoginUrl();
        return ResponseEntity.ok(url);
    }

    // 2) 카카오에서 redirect 해줄 콜백 엔드포인트
    //   Redirect URI = http://localhost:8080/api/auth/kakao/callback
    @GetMapping("/callback")
    public ResponseEntity<LoginResponse> callback(@RequestParam("code") String code) {
        System.out.println("🔥🔥🔥 콜백 도착! code = " + code);
        LoginResponse response = kakaoAuthService.handleCallback(code);
        return ResponseEntity.ok(response);
    }
}
