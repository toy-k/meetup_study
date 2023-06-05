package com.example.meetup_study.auth.exception;

import com.example.meetup_study.common.exception.NotFoundException;

public class AccessTokenNotFoundException extends NotFoundException {
    public AccessTokenNotFoundException() {
        super("엑세스 토큰이 없습니다");
    }
}
