package com.example.meetup_study.review.exception;

import com.example.meetup_study.common.exception.UnauthenticatedException;

public class ReviewUnauthenticationedException extends UnauthenticatedException {
    public ReviewUnauthenticationedException() {
        super("Review 작업을 수행 인증 되지 않았습니다");
    }
}
