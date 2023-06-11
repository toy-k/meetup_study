package com.example.meetup_study.hostReview.domain.dto;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostUser.domain.HostUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@RequiredArgsConstructor
public class HostReviewDto {

    @Positive
    @NotNull(message = "id는 필수 입력 값입니다.")
    private Long id;

    @NotNull(message = "room_id는 필수 입력 값입니다.")
    private Long roomId;

    @NotNull(message = "review_id는 필수 입력 값입니다.")
    private Long reviewId;

    @NotNull(message = "userId는 null이 될 수 없습니다.")
    private Long userId;

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

