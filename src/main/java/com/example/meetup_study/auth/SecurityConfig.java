package com.example.meetup_study.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DefaultOAuth2UserService customOauth2UserService;
    private final AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.debug("[SecurityConfig] configure()");

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable();

        http
                .authorizeRequests()
                .antMatchers("/api/room", "/api/user/me").access("hasRole('ROLE_USER')")
                .anyRequest().permitAll()
                .and() //localhost:8080/login
                    .oauth2Login().userInfoEndpoint().userService(customOauth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);
    }
}
