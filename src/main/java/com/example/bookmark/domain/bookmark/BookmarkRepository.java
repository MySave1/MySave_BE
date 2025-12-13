package com.example.bookmark.domain.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    // 유저별 북마크 목록
    List<Bookmark> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 🔔 아직 발송 안 된 리마인더만 조회
    List<Bookmark> findByReminderAtBeforeAndReminderSentFalse(OffsetDateTime now);
}
