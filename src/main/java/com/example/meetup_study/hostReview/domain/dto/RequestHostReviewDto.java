package com.example.meetup_study.hostReview.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestHostReviewDto {

    @Schema(description = "호스트 리뷰 룸 id", example = "1", required = true)
    @NotNull(message = "room_id는 null이 될 수 없습니다.")
    private Long roomId;

    @Schema(description = "호스트 리뷰 내용", example = "호스트 리뷰 내용", required = true)
    @Lob
    private String content;

    @Schema(description = "리뷰  id", example = "1", required = true)
    @NotNull(message = "review_id는 null이 될 수 없습니다.")
    private Long reviewId;

}
