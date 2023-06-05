package com.example.meetup_study.auth.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class RefreshTokenInvalidRequestException extends InvalidRequestException {

    public RefreshTokenInvalidRequestException() {
        super("유효하지 않은 리프레시 토큰 입니다");
    }
}
