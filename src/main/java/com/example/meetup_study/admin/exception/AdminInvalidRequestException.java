package com.example.meetup_study.admin.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class AdminInvalidRequestException extends InvalidRequestException {
    public AdminInvalidRequestException() {
        super("유효하지 않은 Admin 요청입니다.");
    }
}
