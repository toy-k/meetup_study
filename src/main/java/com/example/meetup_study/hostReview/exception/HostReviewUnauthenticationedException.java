package com.example.meetup_study.hostReview.exception;

import com.example.meetup_study.common.exception.UnauthenticatedException;

public class HostReviewUnauthenticationedException extends UnauthenticatedException {
    public HostReviewUnauthenticationedException() {
        super("HostReview 작업을 수행 인증 되지 않았습니다");
    }
}
