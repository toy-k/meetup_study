package com.example.meetup_study.hostReview.domain.dto;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostUser.domain.HostUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@RequiredArgsConstructor
public class HostReviewDto {

    @Positive
    @NotBlank(message = "id는 필수 입력 값입니다.")
    private Long id;

    @NotBlank(message = "room_id는 필수 입력 값입니다.")
    private Long roomId;

    @NotBlank(message = "review_id는 필수 입력 값입니다.")
    private Long reviewId;

    @Lob
    private String content;

    public HostReviewDto(Long id, Long roomId, Long reviewId, String content) {
        this.id = id;
        this.roomId = roomId;
        this.reviewId = reviewId;
        this.content = content;
    }

    public HostReviewDto convertToHostReviewDto(HostReview hostReview) {
        return new HostReviewDto(hostReview.getId(), hostReview.getRoom().getId(), hostReview.getReviewId(), hostReview.getContent());
    }

}

