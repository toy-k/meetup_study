package com.example.meetup_study.admin.exception;

import com.example.meetup_study.common.exception.ForbiddenException;

public class AdminForbiddenException extends ForbiddenException {
    public AdminForbiddenException() {
        super("Admin 작업 금지 되었습니다.");
    }
}
