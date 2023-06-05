package com.example.meetup_study.room.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class RoomInvalidRequestException extends InvalidRequestException {
    public RoomInvalidRequestException(String msg) {
        super("유효하지 않은 Room 요청입니다." + msg);
    }
}
