package com.example.meetup_study.hostReview.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class RequestHostReviewDto {

    @NotNull(message = "room_id는 null이 될 수 없습니다.")
    private Long roomId;

    @Lob
    private String content;

    @NotNull(message = "review_id는 null이 될 수 없습니다.")
    private Long reviewId;

    public RequestHostReviewDto(Long roomId, String content, Long reviewId) {
        this.roomId = roomId;
        this.content = content;
        this.reviewId = reviewId;
    }
}
