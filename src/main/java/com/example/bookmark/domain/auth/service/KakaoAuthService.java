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

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoProperties kakaoProperties;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final RestTemplate restTemplate = new RestTemplate();

    // 로그인 URL 생성 (clientRedirectUri 우선 적용)
    public String generateLoginUrl(String clientRedirectUri) {

        String finalRedirectUri = (clientRedirectUri != null && !clientRedirectUri.isBlank())
                ? clientRedirectUri
                : kakaoProperties.getRedirectUri();

        return UriComponentsBuilder.fromHttpUrl(kakaoProperties.getAuthUrl())
                .queryParam("client_id", kakaoProperties.getClientId())
                .queryParam("redirect_uri", finalRedirectUri)
                .queryParam("response_type", "code")
                .build()
                .toUriString();
    }

    // 콜백 처리
    public LoginResponse handleCallback(String code, String clientRedirectUri) {

        String finalRedirectUri = (clientRedirectUri != null && !clientRedirectUri.isBlank())
                ? clientRedirectUri
                : kakaoProperties.getRedirectUri();

        KakaoTokenResponse tokenResponse = requestAccessToken(code, finalRedirectUri);

        KakaoUserInfoResponse userInfo = requestUserInfo(tokenResponse.getAccessToken());

        Long kakaoId = userInfo.getId();
        String email = userInfo.getEmail();
        String nickname = userInfo.getNickname();

        User user = findOrCreateOrUpdateUser(kakaoId, email, nickname);

        String jwt = jwtTokenProvider.generateToken(user.getId());

        return new LoginResponse(jwt, user.getId(), user.getName(), user.getEmail());
    }

    // 토큰 요청
    private KakaoTokenResponse requestAccessToken(String code, String redirectUri) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getClientId());
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                kakaoProperties.getTokenUrl(),
                HttpMethod.POST,
                entity,
                KakaoTokenResponse.class
        );

        return response.getBody();
    }

    // 유저 정보 요청
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

    // 유저 저장 + 업데이트
    private User findOrCreateOrUpdateUser(Long kakaoId, String email, String nickname) {

        User user = userRepository.findByKakaoId(kakaoId)
                .orElse(new User());

        if (user.getId() == null) {
            user.setKakaoId(kakaoId);
            user.setProvider("kakao");
            user.setCreatedAt(OffsetDateTime.now());
        }

        String safeEmail = (email != null)
                ? email
                : "kakao-user-" + kakaoId + "@noemail.kakao";

        user.setEmail(safeEmail);

        if (nickname != null && !nickname.isBlank()) {
            user.setName(nickname);
        } else if (user.getName() == null) {
            user.setName("카카오사용자");
        }

        user.setUpdatedAt(OffsetDateTime.now());

        return userRepository.save(user);
    }
}
