package com.example.bookmark.domain.bookmark.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class BookmarkUpdateRequest {

    private String url;
    private String title;

    private String content;   // 원본 텍스트
    private String summary;   // 요약문
    private String memo;      // 메모

    private List<String> tags;

    private OffsetDateTime reminderAt;
}