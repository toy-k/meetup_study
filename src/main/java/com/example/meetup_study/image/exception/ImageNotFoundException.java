package com.example.meetup_study.image.exception;

import com.example.meetup_study.common.exception.NotFoundException;

public class ImageNotFoundException extends NotFoundException {

    public ImageNotFoundException() {
        super("유효하지 않은 Image 입니다.");
    }
}
