package com.example.meetup_study.user.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class FakeUserInvalidRequestException extends InvalidRequestException {
    public FakeUserInvalidRequestException() {
        super("유효하지 않은 가짜 유저 요청입니다.");
    }
}
