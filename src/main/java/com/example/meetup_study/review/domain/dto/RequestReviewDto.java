package com.example.meetup_study.review.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestReviewDto {

    @Schema(description = "룸 id", example = "1", required = true)
    @NotNull(message = "room_id는 null이 될 수 없습니다.")
    private Long roomId;

    @Schema(description = "평점", example = "5", required = true)
    @Min(1)
    @Max(5)
    @NotNull(message = "rating은 null이 될 수 없습니다.")
    private Integer rating;

    @Schema(description = "리뷰 내용", example = "리뷰 내용", required = true)
    @Lob
    private String content;

}
