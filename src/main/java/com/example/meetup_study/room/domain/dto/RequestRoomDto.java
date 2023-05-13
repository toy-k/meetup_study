package com.example.meetup_study.room.domain.dto;

import com.example.meetup_study.room.domain.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class RequestRoomDto {



    @NotBlank(message = "title은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "description은 필수 입력 값입니다.")
    private String description;

    @NotNull(message = "joinEndDate은 필수 입력 값입니다.")
    private LocalDateTime joinEndDate;

    @NotNull(message = "meetupStartDate은 필수 입력 값입니다.")
    private LocalDateTime meetupStartDate;

    @NotNull(message = "meetupEndDate은 필수 입력 값입니다.")
    private LocalDateTime meetupEndDate;

    @NotNull(message = "meetupLocation은 필수 입력 값입니다.")
    private String meetupLocation;

    private String meetupPhotoUrl;

    @NotBlank(message = "category은 필수 입력 값입니다.")
    private Category category;

    @Positive
    private Long hostUserId;

    @Min(2)
    @Positive
    private Integer joinNumber;



    //생성자
    public RequestRoomDto(String title, String description, LocalDateTime joinEndDate, LocalDateTime meetupStartDate, LocalDateTime meetupEndDate, String meetupLocation, String meetupPhotoUrl, Category category, Long hostUserId, Integer joinNumber) {
        this.title = title;
        this.description = description;
        this.joinEndDate = joinEndDate;
        this.meetupStartDate = meetupStartDate;
        this.meetupEndDate = meetupEndDate;
        this.meetupLocation = meetupLocation;
        this.meetupPhotoUrl = meetupPhotoUrl;
        this.category = category;
        this.hostUserId = hostUserId;
        this.joinNumber = joinNumber;
    }
}
