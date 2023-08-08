package com.example.meetup_study.announce.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestAnnounceDto {

    @Schema(description = "공지사항 제목", example = "공지사항 제목", required = true)
    @NotNull(message = "title은 필수 입력 값입니다.")
    private String title;

    @Schema(description = "공지사항 내용", example = "공지사항 내용", required = true)
    @NotNull(message = "description은 필수 입력 값입니다.")
    private String description;

    @Schema(description = "공지사항 작성자 id", example = "1", required = true)
    @NotNull(message = "userId은 필수 입력 값입니다.")
    private Long userId;

}
