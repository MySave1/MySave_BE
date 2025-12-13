package com.example.bookmark.domain.bookmark.dto;

import com.example.bookmark.domain.bookmark.Bookmark;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Builder
public class BookmarkResponse {

    private Long id;
    private Long userId;
    private String url;
    private String title;
    private String content;
    private String summary;
    private String memo;
    private List<String> tags;
    private OffsetDateTime reminderAt;
    private OffsetDateTime createdAt;

    public static BookmarkResponse from(Bookmark b) {
        return BookmarkResponse.builder()
                .id(b.getId())
                .userId(b.getUserId())
                .url(b.getUrl())
                .title(b.getTitle())
                .content(b.getContent())
                .summary(b.getSummary())
                .memo(b.getMemo())
                .tags(b.getTags())
                .reminderAt(b.getReminderAt())
                .createdAt(b.getCreatedAt())
                .build();
    }
}
