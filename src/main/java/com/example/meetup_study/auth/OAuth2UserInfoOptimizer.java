package com.example.meetup_study.auth;

import com.example.meetup_study.user.domain.ProviderType;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Objects;

public class OAuth2UserInfoOptimizer {

    public static CustomOauth2UserInfo optimize(ProviderType providerType, Map<String, Object> attributes){
        switch (providerType){
            case GOOGLE:
                return new GoogleOauth2UserInfo(attributes);
            default:
                throw new IllegalArgumentException("지원하지 않는 소셜 로그인Provider 입니다.");
        }
    }
}
