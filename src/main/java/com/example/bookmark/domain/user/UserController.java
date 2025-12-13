package com.example.bookmark.domain.user;

import com.example.bookmark.config.JwtTokenProvider;
import com.example.bookmark.domain.user.dto.NotificationEmailRequest;
import com.example.bookmark.domain.user.dto.UserMeResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 🔔 알림 이메일 설정
    @PatchMapping("/notification-email")
    public ResponseEntity<?> updateNotificationEmail(
            @RequestBody NotificationEmailRequest request,
            HttpServletRequest httpRequest
    ) {
        Long userId = jwtTokenProvider.getUserId(httpRequest);
        userService.updateNotificationEmail(userId, request);
        return ResponseEntity.ok().build();
    }

    // ✅ 내 정보 조회 (notificationEmail 포함)
    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> getMyInfo(HttpServletRequest request) {

        Long userId = jwtTokenProvider.getUserId(request);
        UserMeResponse response = userService.getMyInfo(userId);

        return ResponseEntity.ok(response);
    }
}