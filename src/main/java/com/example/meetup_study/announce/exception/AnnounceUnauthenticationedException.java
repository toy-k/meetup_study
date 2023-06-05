package com.example.meetup_study.announce.exception;

import com.example.meetup_study.common.exception.UnauthenticatedException;

public class AnnounceUnauthenticationedException extends UnauthenticatedException {
    public AnnounceUnauthenticationedException() {
        super("Announce 작업을 수행 인증 되지 않았습니다");
    }
}
