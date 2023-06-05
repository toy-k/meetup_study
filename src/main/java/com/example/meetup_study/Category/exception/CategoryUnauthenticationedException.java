package com.example.meetup_study.Category.exception;

import com.example.meetup_study.common.exception.UnauthenticatedException;

public class CategoryUnauthenticationedException extends UnauthenticatedException {
    public CategoryUnauthenticationedException() {
        super("Category 작업을 수행 인증 되지 않았습니다");
    }
}
