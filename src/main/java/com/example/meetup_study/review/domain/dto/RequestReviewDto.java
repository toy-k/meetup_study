package com.example.meetup_study.review.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@RequiredArgsConstructor
public class RequestReviewDto {

    @NotNull(message = "user_id는 null이 될 수 없습니다.")
    private Long roomId;

    @Min(1)
    @Max(5)
    @NotNull(message = "rating은 null이 될 수 없습니다.")
    private Integer rating;

    @Lob
    private String content;

    public RequestReviewDto(Long roomId, Integer rating, String content) {
        this.roomId = roomId;
        this.rating = rating;
        this.content = content;
    }
}
