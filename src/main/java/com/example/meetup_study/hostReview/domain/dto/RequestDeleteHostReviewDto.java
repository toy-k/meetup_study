package com.example.meetup_study.hostReview.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class RequestDeleteHostReviewDto {

    @Schema(description = "호스트 리뷰 id", example = "1", required = true)
    @Id
    @NotNull(message = "hostReview_id는 null이 될 수 없습니다.")
    private Long hostReviewId;

}
