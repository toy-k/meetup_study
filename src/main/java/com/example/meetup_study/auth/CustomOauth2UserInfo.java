package com.example.meetup_study.auth;

import lombok.Getter;

import java.util.Map;


public abstract class CustomOauth2UserInfo {
    protected Map<String, Object> attributes;

    public CustomOauth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String,Object> getAttributes(){
        return attributes;
    }

    public abstract String getId();
    public abstract String getName();
    public abstract String getEmail();
    public abstract String getImageUrl();
}
