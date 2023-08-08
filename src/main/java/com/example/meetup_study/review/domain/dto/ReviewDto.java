package com.example.meetup_study.review.domain.dto;

import com.example.meetup_study.review.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewDto {

    @Schema(description = "리뷰 id", example = "1", required = true)
    @Positive
    @NotNull(message = "id는 필수 입력 값입니다.")
    private Long id;

    @Schema(description = "유저 id", example = "1", required = true)
    @NotNull(message = "userId는 null이 될 수 없습니다.")
    private Long userId;

    @Schema(description = "방 id", example = "1", required = true)
    @NotNull(message = "roomId는 null이 될 수 없습니다.")
    private Long roomId;


    @Schema(description = "리뷰 평점", example = "1", required = true)
    @Positive
    @Min(1)
    @Max(5)
    private Integer rating;

    @Schema(description = "리뷰 내용", example = "리뷰 내용", required = true)
    @Lob
    private String content;

}
