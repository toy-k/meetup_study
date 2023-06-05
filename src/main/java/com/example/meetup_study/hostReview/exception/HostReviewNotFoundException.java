package com.example.meetup_study.hostReview.exception;

import com.example.meetup_study.common.exception.NotFoundException;

public class HostReviewNotFoundException extends NotFoundException {

    public HostReviewNotFoundException() {
        super("유효하지 않은 HostReview 입니다.");
    }
}
