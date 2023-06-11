package com.example.meetup_study.image.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class ImageInvalidRequestException extends InvalidRequestException {
    public ImageInvalidRequestException(String msg) {
        super("유효하지 않은 Image 요청입니다. "+msg);
    }
    public ImageInvalidRequestException() {
        super("유효하지 않은 Image 요청입니다.");
    }
}
