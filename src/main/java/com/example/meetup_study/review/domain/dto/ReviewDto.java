package com.example.meetup_study.review.domain.dto;

import com.example.meetup_study.review.domain.Review;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.*;

@Getter
@RequiredArgsConstructor
public class ReviewDto {

    @Positive
    @NotBlank(message = "id는 필수 입력 값입니다.")
    private Long id;

    @NotNull(message = "userId는 null이 될 수 없습니다.")
    private Long userId;

    @NotNull(message = "roomId는 null이 될 수 없습니다.")
    private Long roomId;



    @Positive
    @Min(1)
    @Max(5)
    private Integer rating;

    @Lob
    private String content;

    public ReviewDto(Long id, Long userId,Long roomId, Integer rating, String content) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.rating = rating;
        this.content = content;
    }

    public ReviewDto convertToReviewDto(Review review) {
        return new ReviewDto(review.getId(), review.getUser().getId(),review.getRoom().getId(), review.getRating(), review.getContent());
    }
}
