package com.example.meetup_study.review.exception;

import com.example.meetup_study.common.exception.NotFoundException;

public class ReviewNotFoundException extends NotFoundException {

    public ReviewNotFoundException() {
        super("유효하지 않은 Review 입니다.");
    }
}
