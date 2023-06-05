package com.example.meetup_study.hostUser.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class HostUserInvalidRequestException extends InvalidRequestException {
    public HostUserInvalidRequestException() {
        super("유효하지 않은 HostUser 요청입니다.");
    }
}
