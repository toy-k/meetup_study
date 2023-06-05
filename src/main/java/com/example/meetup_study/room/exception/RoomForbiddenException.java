package com.example.meetup_study.room.exception;

import com.example.meetup_study.common.exception.ForbiddenException;

public class RoomForbiddenException extends ForbiddenException {
    public RoomForbiddenException() {
        super("Room 작업 금지 되었습니다.");
    }
}
