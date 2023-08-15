package com.example.meetup_study.category.exception;

import com.example.meetup_study.common.exception.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException() {
        super("유효하지 않은 Category 입니다.");
    }
}
