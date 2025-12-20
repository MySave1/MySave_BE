package com.example.bookmark.domain.auth.controller;

import com.example.bookmark.domain.auth.dto.LoginResponse;
import com.example.bookmark.domain.auth.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    // 카카오 로그인 URL 요청 시 -> 프론트 redirect_uri를 받을 수 있도록 변경
    @GetMapping("/login-url")
    public String getLoginUrl(
            @RequestParam(value = "redirect_uri", required = false) String clientRedirectUri
    ) {
        return kakaoAuthService.generateLoginUrl(clientRedirectUri);
    }

    // 카카오 redirect callback -> 프론트로 다시 redirect
    @GetMapping("/callback")
    public void callback(
            @RequestParam("code") String code,
            @RequestParam(value = "redirect_uri", required = false) String clientRedirectUri,
            HttpServletResponse response
    ) throws IOException {

        LoginResponse login = kakaoAuthService.handleCallback(code, clientRedirectUri);

        // callback.html로 리다이렉트
        response.sendRedirect(
                clientRedirectUri
                        + "?token=" + login.getJwt()
                        + "&userId=" + login.getUserId()
                        + "&name=" + login.getName()
        );
    }
}