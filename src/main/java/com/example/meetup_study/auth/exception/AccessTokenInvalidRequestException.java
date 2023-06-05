package com.example.meetup_study.auth.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class AccessTokenInvalidRequestException extends InvalidRequestException {
    public AccessTokenInvalidRequestException() {
        super("유효하지 않은 엑세스 토큰 입니다");
    }

}
