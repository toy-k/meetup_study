package com.example.meetup_study.auth;

import com.example.meetup_study.auth.exception.OAuth2InvalidRequestException;
import com.example.meetup_study.user.domain.enums.ProviderType;

import java.util.Map;

public class OAuth2UserInfoOptimizer {

    public static CustomOauth2UserInfo optimize(ProviderType providerType, Map<String, Object> attributes){
        switch (providerType){
            case GOOGLE:
                return new GoogleOauth2UserInfo(attributes);
            default:
                throw new OAuth2InvalidRequestException();
        }
    }
}
