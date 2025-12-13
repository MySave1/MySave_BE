package com.example.bookmark.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private Key key;

    @PostConstruct
    public void init() {
        // 실제 운영에서는 application.yml 에서 시크릿 키 가져오는 게 좋음
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    /**
     * 🔐 JWT 토큰 생성
     */
    public String generateToken(Long userId) {
        Instant now = Instant.now();
        Instant expiry = now.plus(7, ChronoUnit.DAYS); // 7일짜리 토큰

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(key)
                .compact();
    }

    /**
     * ✅ HttpServletRequest에서 userId 추출 (컨트롤러용)
     */
    public Long getUserId(HttpServletRequest request) {
        String token = resolveToken(request);
        return getUserIdFromToken(token);
    }

    /**
     * 🔍 토큰에서 userId(subject) 파싱
     */
    private Long getUserIdFromToken(String token) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.parseLong(subject);
    }

    /**
     * 🧩 Authorization 헤더에서 토큰 추출
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization 헤더가 없거나 Bearer 토큰이 아닙니다.");
        }

        return bearerToken.substring(7);
    }
}