package com.example.meetup_study.hostReview.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class RequestDeleteHostReviewDto {
    @Id
    @NotNull(message = "hostReview_id는 null이 될 수 없습니다.")
    private Long hostReviewId;

}
