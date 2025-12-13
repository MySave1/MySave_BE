package com.example.bookmark.domain.user;

import com.example.bookmark.domain.user.dto.NotificationEmailRequest;
import com.example.bookmark.domain.user.dto.UserMeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 🔔 알림용 이메일 설정 / 변경
     */
    @Transactional
    public void updateNotificationEmail(Long userId, NotificationEmailRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setNotificationEmail(request.getNotificationEmail());
        // save() 호출 필요 없음 (JPA Dirty Checking)
    }

    /**
     * ✅ 내 정보 조회 (GET /api/users/me)
     */
    @Transactional(readOnly = true)
    public UserMeResponse getMyInfo(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new UserMeResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getNotificationEmail()
        );
    }
}