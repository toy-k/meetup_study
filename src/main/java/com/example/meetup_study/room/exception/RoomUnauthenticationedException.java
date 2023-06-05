package com.example.meetup_study.room.exception;

import com.example.meetup_study.common.exception.UnauthenticatedException;

public class RoomUnauthenticationedException extends UnauthenticatedException {
    public RoomUnauthenticationedException() {
        super("Room 작업을 수행 인증 되지 않았습니다");
    }
}
