package com.example.meetup_study.auth;

import com.example.meetup_study.auth.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler{


    private final JwtService jwtServiceImpl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("[OAuth2AuthenticationSuccessHandler] onAuthenticationSuccess()");

        try{
            UserPrincipal customOAuth2User = (UserPrincipal) authentication.getPrincipal();
            loginSuccess(response, customOAuth2User);
            response.sendRedirect("/");
        }catch (Exception e){
             throw new RuntimeException(e);
        }
    }

    private void loginSuccess(HttpServletResponse response, UserPrincipal customOAuth2User) {
        log.debug("[OAuth2AuthenticationSuccessHandler] loginSuccess()");

        String accessToken = "Bearer " + jwtServiceImpl.generateAccessToken(customOAuth2User.getEmail(), customOAuth2User.getUserId());
        String refreshToken = "Bearer " + jwtServiceImpl.generateRefreshToken(customOAuth2User.getEmail(), customOAuth2User.getUserId());

        jwtServiceImpl.setResponseAccessToken(response, accessToken);
        jwtServiceImpl.setResponseRefreshToken(response, refreshToken);

        log.debug("[OAuth2AuthenticationSuccessHandler] accessToken: {}", accessToken);
        log.debug("[OAuth2AuthenticationSuccessHandler] refreshToken: {}", refreshToken);


    }
}
