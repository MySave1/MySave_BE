package com.example.bookmark.domain.bookmark;

import com.example.bookmark.domain.bookmark.dto.BookmarkCreateRequest;
import com.example.bookmark.domain.bookmark.dto.BookmarkResponse;
import com.example.bookmark.domain.bookmark.dto.BookmarkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    // -------------------------
    // ① 생성
    // -------------------------
    @Transactional
    public BookmarkResponse create(BookmarkCreateRequest req) {
        Bookmark b = new Bookmark();
        b.setUserId(req.getUserId());
        b.setUrl(req.getUrl());
        b.setTitle(req.getTitle());
        b.setContent(req.getContent());
        b.setSummary(req.getSummary());
        b.setMemo(req.getMemo());
        b.setReminderAt(req.getReminderAt());

        if (req.getTags() != null) {
            b.setTags(new ArrayList<>(req.getTags()));
        }

        return BookmarkResponse.from(bookmarkRepository.save(b));
    }

    // -------------------------
    // ② 단건 조회
    // -------------------------
    @Transactional(readOnly = true)
    public BookmarkResponse get(Long id) {
        Bookmark b = bookmarkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));
        return BookmarkResponse.from(b);
    }

    // -------------------------
    // ③ 리스트 조회
    // -------------------------
    @Transactional(readOnly = true)
    public List<BookmarkResponse> listByUser(Long userId) {
        return bookmarkRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(BookmarkResponse::from)
                .toList();
    }

    // -------------------------
    // ④ 수정(PATCH)
    // -------------------------
    @Transactional
    public BookmarkResponse update(Long id, BookmarkUpdateRequest req) {

        Bookmark b = bookmarkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        if (req.getUrl() != null) b.setUrl(req.getUrl());
        if (req.getTitle() != null) b.setTitle(req.getTitle());
        if (req.getContent() != null) b.setContent(req.getContent());
        if (req.getSummary() != null) b.setSummary(req.getSummary());
        if (req.getMemo() != null) b.setMemo(req.getMemo());
        if (req.getReminderAt() != null) b.setReminderAt(req.getReminderAt());

        if (req.getTags() != null) {
            b.setTags(new ArrayList<>(req.getTags()));
        }

        return BookmarkResponse.from(bookmarkRepository.save(b));
    }

    // -------------------------
    // ⑤ 삭제
    // -------------------------
    @Transactional
    public void delete(Long id) {
        Bookmark b = bookmarkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        bookmarkRepository.delete(b);
    }
}