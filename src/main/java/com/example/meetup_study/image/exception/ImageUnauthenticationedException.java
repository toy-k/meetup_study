package com.example.meetup_study.image.exception;

import com.example.meetup_study.common.exception.UnauthenticatedException;

public class ImageUnauthenticationedException extends UnauthenticatedException {
    public ImageUnauthenticationedException() {
        super("Image 작업을 수행 인증 되지 않았습니다");
    }
}
