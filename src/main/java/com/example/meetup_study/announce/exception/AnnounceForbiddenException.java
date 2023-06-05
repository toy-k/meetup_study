package com.example.meetup_study.announce.exception;

import com.example.meetup_study.common.exception.ForbiddenException;

public class AnnounceForbiddenException extends ForbiddenException {
    public AnnounceForbiddenException() {
        super("Announce 작업 금지 되었습니다.");
    }
}
