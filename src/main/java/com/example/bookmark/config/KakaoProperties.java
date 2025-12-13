package com.example.bookmark.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kakao")
@Getter
@Setter
public class KakaoProperties {

    private String clientId;
    private String redirectUri;
    private String authUrl;
    private String tokenUrl;
    private String userInfoUrl;
}
