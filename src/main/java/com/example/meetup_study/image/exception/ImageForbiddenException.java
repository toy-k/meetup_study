package com.example.meetup_study.image.exception;

import com.example.meetup_study.common.exception.ForbiddenException;

public class ImageForbiddenException extends ForbiddenException {
    public ImageForbiddenException() {
        super("Image 작업 금지 되었습니다.");
    }
}
