package com.example.meetup_study.common.interceptor;

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
                .addPathPatterns("/api/joinedUser")
                .addPathPatterns("/api/review")
                .addPathPatterns("/api/hostReview")
                .addPathPatterns("/api/announce")
                .addPathPatterns("/api/admin/**")
                .addPathPatterns("/api/userImage")
                .addPathPatterns("/api/roomImage")
                .addPathPatterns("/api/announceImage")
                .addPathPatterns("/api/cart")
                .addPathPatterns("/api/user/me") // user/me put, get
                .excludePathPatterns("/api/room/id/**")
                .excludePathPatterns("/api/room/list")
                .excludePathPatterns("/api/room/list/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/test/**")
                .excludePathPatterns("/swagger-ui")
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/api/init")
                .excludePathPatterns("/api/room/count")
        ;



    }
}
