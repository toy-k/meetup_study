package com.example.meetup_study.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor{

    private String AUTHORIZATION = "Authorization";
    private String ACCESSTOKEN = "AccessToken";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("[JwtInterceptor] preHandle()");

        if(request.getMethod().equals(HttpMethod.OPTIONS.name())){
            return true;
        }

        String tokenInHeader = request.getHeader(AUTHORIZATION);
        String tokenInAttr = (String)request.getAttribute(AUTHORIZATION);

        String accessToken;

        if(tokenInHeader != null && !tokenInHeader.split(" ")[1].equals("null")) {
            accessToken = tokenInHeader.split(" ")[1];
        }else if(tokenInAttr != null && !tokenInAttr.equals("null")) {
            accessToken = tokenInAttr.split(" ")[1];
        } else {
            throw new Exception("토큰이 존재하지 않습니다.");
        }

        request.setAttribute(ACCESSTOKEN, accessToken);

        return true;
    }
}
