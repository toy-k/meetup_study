package com.example.meetup_study.user.exception;

import com.example.meetup_study.common.exception.ForbiddenException;

public class FakeUserForbiddenException extends ForbiddenException {
    public FakeUserForbiddenException() {
        super("가짜 유저는 이 작업 금지 되었습니다.");
    }
}
