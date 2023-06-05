package com.example.meetup_study.announce.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class AnnounceInvalidRequestException extends InvalidRequestException {
    public AnnounceInvalidRequestException() {
        super("유효하지 않은 Announce 요청입니다.");
    }
}
