package com.example.meetup_study.auth.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class OAuth2InvalidRequestException extends InvalidRequestException {

        public OAuth2InvalidRequestException() {
            super("유효하지 않은 OAuth2 인증입니다");
        }
}
