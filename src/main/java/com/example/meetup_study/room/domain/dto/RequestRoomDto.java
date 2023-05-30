package com.example.meetup_study.room.domain.dto;

import com.example.meetup_study.Category.domain.CategoryEnum;
import com.example.meetup_study.room.domain.RoomStatus;
import com.example.meetup_study.room.domain.RoomType;
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

    @NotBlank(message = "category은 필수 입력 값입니다.")
    private CategoryEnum category;

    @NotNull(message = "location은 필수 입력 값입니다.")
    private String location;

    @NotNull(message = "meetupStartDate은 필수 입력 값입니다.")
    private LocalDateTime meetupStartDate;

    @NotNull(message = "meetupEndDate은 필수 입력 값입니다.")
    private LocalDateTime meetupEndDate;

    @Min(1)
    @Positive
    private Integer maxJoinNumber;

    @Min(1)
    @Positive
    private Integer currentJoinNumber;

    @Positive
    private Long price;

    @NotBlank(message = "roomStatus은 필수 입력 값입니다.")
    private RoomStatus roomStatus;

    @NotBlank(message = "roomType은 필수 입력 값입니다.")
    private RoomType roomType; //Online, Offline

    private String meetupPhotoPath;

    @Positive
    private Long viewCount;


    ///////////////////////////////


    @Positive
    private Long hostUserId;



    //생성자
    public RequestRoomDto(String title, String description, CategoryEnum category, String location, LocalDateTime meetupStartDate, LocalDateTime meetupEndDate, Integer maxJoinNumber, Integer currentJoinNumber, Long price, RoomStatus roomStatus,RoomType roomType, Long viewCount,String meetupPhotoPath, Long hostUserId) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.meetupStartDate = meetupStartDate;
        this.meetupEndDate = meetupEndDate;
        this.maxJoinNumber = maxJoinNumber;
        this.currentJoinNumber = currentJoinNumber;
        this.price = price;
        this.roomStatus = roomStatus;
        this.roomType = roomType;
        this.viewCount = viewCount;
        this.meetupPhotoPath = meetupPhotoPath;

        this.hostUserId = hostUserId;
    }
}