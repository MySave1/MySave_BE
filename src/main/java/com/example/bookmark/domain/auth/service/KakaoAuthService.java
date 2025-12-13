package com.example.bookmark.domain.auth.service;

import com.example.bookmark.config.KakaoProperties;
import com.example.bookmark.config.JwtTokenProvider;
import com.example.bookmark.domain.auth.dto.KakaoTokenResponse;
import com.example.bookmark.domain.auth.dto.KakaoUserInfoResponse;
import com.example.bookmark.domain.auth.dto.LoginResponse;
import com.example.bookmark.domain.user.User;
import com.example.bookmark.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoProperties kakaoProperties;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final RestTemplate restTemplate = new RestTemplate();

    // 1) 로그인 URL 생성
    public String generateLoginUrl() {
        return UriComponentsBuilder.fromHttpUrl(kakaoProperties.getAuthUrl())
                .queryParam("client_id", kakaoProperties.getClientId())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
                .queryParam("response_type", "code")
                .build()
                .toUriString();
    }

    // 2~6) 콜백 처리: 토큰 교환, 유저정보, DB저장, JWT발급
    public LoginResponse handleCallback(String code) {

        // 3) code → access_token
        KakaoTokenResponse tokenResponse = requestAccessToken(code);

        // 4) access_token → 유저 정보
        KakaoUserInfoResponse userInfo = requestUserInfo(tokenResponse.getAccessToken());

        Long kakaoId = userInfo.getId();
        String email = userInfo.getEmail();

        // 5) 우리 DB에서 User 조회/생성
        User user = findOrCreateUser(kakaoId, email);

        // 6) JWT 발급
        String jwt = jwtTokenProvider.generateToken(user.getId());

        return new LoginResponse(jwt, user.getId(), user.getName(), user.getEmail());
    }

    private KakaoTokenResponse requestAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getClientId());
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("code", code);

        // 🔥🔥🔥 여기에서 로그 출력해야 함 ============
        System.out.println("📌 Sending Token Request to Kakao");
        System.out.println("📌 grant_type = authorization_code");
        System.out.println("📌 client_id = " + kakaoProperties.getClientId());
        System.out.println("📌 redirect_uri = " + kakaoProperties.getRedirectUri());
        System.out.println("📌 code = " + code);
        System.out.println("📌 token_url = " + kakaoProperties.getTokenUrl());
        // =========================================

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                kakaoProperties.getTokenUrl(),
                HttpMethod.POST,
                entity,
                KakaoTokenResponse.class
        );

        return response.getBody();
    }

    private KakaoUserInfoResponse requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfoResponse> response = restTemplate.exchange(
                kakaoProperties.getUserInfoUrl(),
                HttpMethod.GET,
                entity,
                KakaoUserInfoResponse.class
        );

        return response.getBody();
    }

    private User findOrCreateUser(Long kakaoId, String email) {

        // 이미 있는 유저면 바로 반환
        Optional<User> existing = userRepository.findByKakaoId(kakaoId);
        if (existing.isPresent()) {
            return existing.get();
        }

        // email이 null일 때 대비
        String safeEmail = (email != null)
                ? email
                : "kakao-user-" + kakaoId + "@noemail.kakao";

        User user = new User();
        user.setKakaoId(kakaoId);
        user.setEmail(safeEmail);
        user.setName("카카오사용자");
        user.setProvider("kakao");
        user.setCreatedAt(OffsetDateTime.now());

        return userRepository.save(user);
    }
}
