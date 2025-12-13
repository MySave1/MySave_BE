package com.example.bookmark.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;   // 우리 서비스 JWT
    private Long userId;
    private String name;
    private String email;
}
