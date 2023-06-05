package com.example.meetup_study.hostUser.exception;

import com.example.meetup_study.common.exception.ForbiddenException;

public class HostUserForbiddenException extends ForbiddenException {
    public HostUserForbiddenException() {
        super("HostUser 작업 금지 되었습니다.");
    }
}
