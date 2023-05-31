package com.example.meetup_study.announce.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class RequestAnnounceDto {

    @NotBlank(message = "title은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "description은 필수 입력 값입니다.")
    private String description;

    @NotBlank(message = "userId은 필수 입력 값입니다.")
    private Long userId;

    private String imagePath;

    public RequestAnnounceDto(String title, String description, Long userId, String imagePath) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.imagePath = imagePath;
    }

}
