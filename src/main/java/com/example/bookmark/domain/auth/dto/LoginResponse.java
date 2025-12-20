package com.example.bookmark.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String jwt;
    private Long userId;
    private String name;
    private String email;
}