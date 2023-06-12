package com.example.meetup_study.hostReview.domain.dto;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostUser.domain.HostUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@RequiredArgsConstructor
public class HostReviewDto {

    @Schema(description = "호스트 리뷰 id", example = "1", required = true)
    @Positive
    @NotNull(message = "id는 필수 입력 값입니다.")
    private Long id;

    @Schema(description = "호스트 리뷰 룸 id", example = "1", required = true)
    @NotNull(message = "room_id는 필수 입력 값입니다.")
    private Long roomId;

    @Schema(description = "리뷰  id", example = "1", required = true)
    @NotNull(message = "review_id는 필수 입력 값입니다.")
    private Long reviewId;

    @Schema(description = "호스트 리뷰 작성자 id", example = "1", required = true)
    @NotNull(message = "userId는 null이 될 수 없습니다.")
    private Long userId;

    @Schema(description = "호스트 리뷰 내용", example = "호스트 리뷰 내용", required = true)
    @Lob
    private String content;

    public HostReviewDto(Long id, Long userId,Long roomId, Long reviewId, String content) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.reviewId = reviewId;
        this.content = content;
    }

    public HostReviewDto convertToHostReviewDto(HostReview hostReview) {
        return new HostReviewDto(hostReview.getId(),hostReview.getUser().getId(), hostReview.getRoom().getId(), hostReview.getReviewId(), hostReview.getContent());
    }

}

