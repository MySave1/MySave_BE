package com.example.bookmark.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfoResponse {

    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @Setter
    public static class KakaoAccount {

        private String email;

        private KakaoProfile profile;

        @JsonProperty("has_email")
        private boolean hasEmail;

        @JsonProperty("email_needs_agreement")
        private boolean emailNeedsAgreement;
    }

    @Getter
    @Setter
    public static class KakaoProfile {
        private String nickname;
    }

    // 편의 메서드
    public String getEmail() {
        if (kakaoAccount == null) return null;
        return kakaoAccount.getEmail();
    }

    public String getNickname() {
        if (kakaoAccount == null || kakaoAccount.getProfile() == null) return null;
        return kakaoAccount.getProfile().getNickname();
    }
}