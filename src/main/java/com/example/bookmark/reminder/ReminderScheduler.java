package com.example.bookmark.reminder;

import com.example.bookmark.domain.bookmark.Bookmark;
import com.example.bookmark.domain.bookmark.BookmarkRepository;
import com.example.bookmark.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    // 1분마다 실행
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkReminders() {

        List<Bookmark> targets =
                bookmarkRepository.findByReminderAtBeforeAndReminderSentFalse(
                        OffsetDateTime.now()
                );

        for (Bookmark b : targets) {

            var user = userRepository.findById(b.getUserId()).orElse(null);
            if (user == null || user.getNotificationEmail() == null) continue;

            emailService.sendReminder(
                    user.getNotificationEmail(),
                    b.getTitle(),
                    b.getUrl()
            );

            // ✅ 발송 완료 처리
            b.setReminderSent(true);
        }
    }
}
