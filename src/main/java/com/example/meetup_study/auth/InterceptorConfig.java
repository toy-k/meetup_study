package com.example.meetup_study.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("[InterceptorConfig] addInterceptors()");

        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/api/room/**")
                .excludePathPatterns("/api/room/all")
                .excludePathPatterns("/api/room/id/**")
                .excludePathPatterns("/api/room/before-meetup")
                .excludePathPatterns("/api/room/after-meetup")
                .excludePathPatterns("/login");



    }
}
