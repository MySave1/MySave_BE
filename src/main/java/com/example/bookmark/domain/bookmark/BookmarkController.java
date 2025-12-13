package com.example.bookmark.domain.bookmark;

import com.example.bookmark.domain.bookmark.dto.BookmarkCreateRequest;
import com.example.bookmark.domain.bookmark.dto.BookmarkResponse;
import com.example.bookmark.domain.bookmark.dto.BookmarkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // ① 생성
    @PostMapping
    public BookmarkResponse create(@RequestBody BookmarkCreateRequest request) {
        return bookmarkService.create(request);
    }

    // ② 단건 조회
    @GetMapping("/{id}")
    public BookmarkResponse get(@PathVariable Long id) {
        return bookmarkService.get(id);
    }

    // ③ 리스트 조회
    @GetMapping
    public List<BookmarkResponse> list(@RequestParam Long userId) {
        return bookmarkService.listByUser(userId);
    }

    // ④ 수정(PATCH)
    @PatchMapping("/{id}")
    public BookmarkResponse update(
            @PathVariable Long id,
            @RequestBody BookmarkUpdateRequest request
    ) {
        return bookmarkService.update(id, request);
    }

    // ⑤ 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookmarkService.delete(id);
    }
}
