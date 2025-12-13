package com.example.bookmark.reminder;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendReminder(String to, String title, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[Bookmark Reminder] " + title);
        message.setText(
                "🔔 북마크 리마인더 알림입니다.\n\n" +
                        "제목: " + title + "\n" +
                        "URL: " + url + "\n"
        );

        mailSender.send(message);
    }
}
