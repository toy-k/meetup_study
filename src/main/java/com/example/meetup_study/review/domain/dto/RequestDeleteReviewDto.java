package com.example.meetup_study.review.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestDeleteReviewDto {

    @Schema(description = "리뷰 id", example = "1", required = true)
    @Id
    @NotNull(message = "review_id는 null이 될 수 없습니다.")
    private Long reviewId;
}
