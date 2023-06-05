package com.example.meetup_study.auth.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class OAuth2SuccessInvalidRequestException extends InvalidRequestException {

        public OAuth2SuccessInvalidRequestException() {
            super("유효하지 않은 OAuth2 Success 입니다");
        }
}
