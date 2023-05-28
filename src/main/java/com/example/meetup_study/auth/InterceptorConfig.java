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

        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/api/room/**")
                .addPathPatterns("/api/user/me") // user/me put, get
                .excludePathPatterns("/api/room/id/**")
                .excludePathPatterns("/api/room/list")
                .excludePathPatterns("/api/room/list/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/test/**")
                .excludePathPatterns("/swagger-ui")
                .excludePathPatterns("/swagger-ui/**");



    }
}
