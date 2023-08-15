package com.example.meetup_study.room.domain.dto;

import com.example.meetup_study.category.domain.CategoryEnum;
import com.example.meetup_study.room.domain.enums.RoomStatus;
import com.example.meetup_study.room.domain.enums.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestRoomDto {

    @Schema(description = "방 제목", example = "방 제목", required = true)
    @NotBlank(message = "title은 필수 입력 값입니다.")
    private String title;

    @Schema(description = "모임 설명", example = "모임 설명", required = true)
    @NotBlank(message = "description은 필수 입력 값입니다.")
    private String description;

    @Schema(description = "카테고리", example = "IT", required = true)
    @NotNull(message = "category은 필수 입력 값입니다.")
    private CategoryEnum category;

    @Schema(description = "모임 위치", example = "모임 위치", required = true)
    @NotNull(message = "location은 필수 입력 값입니다.")
    private String location;

    @Schema(description = "모임 시작 시간", example = "2021-08-01T00:00:00", required = true)
    @NotNull(message = "meetupStartDate은 필수 입력 값입니다.")
    private LocalDateTime meetupStartDate;

    @Schema(description = "모임 종료 시간", example = "2021-08-01T00:00:00", required = true)
    @NotNull(message = "meetupEndDate은 필수 입력 값입니다.")
    private LocalDateTime meetupEndDate;

    @Schema(description = "최대 참여 인원", example = "10", required = true)
    @Min(1)
    @Positive
    @NotNull(message = "maxJoinNumber은 필수 입력 값입니다.")
    private Integer maxJoinNumber;

    @Schema(description = "현재 참여 인원", example = "1", required = true)
    @Min(1)
    @Positive
    private Integer currentJoinNumber;

    @Schema(description = "모임 참여비", example = "10000", required = true)
    @Positive
    private Long price;

    @Schema(description = "모임 상태", example = "OPEN", required = true)
    @NotNull(message = "roomStatus은 필수 입력 값입니다.")
    private RoomStatus roomStatus;

    @Schema(description = "모임 타입", example = "ONLINE", required = true)
    @NotNull(message = "roomType은 필수 입력 값입니다.")
    private RoomType roomType; //Online, Offline

    @Schema(description = "모임 조회수", example = "1", required = true)
    @Positive
    private Long viewCount;


    ///////////////////////////////
    @Schema(description = "모임 사진 경로", example = "모임 사진 경로", required = true)
    private String imagePath;

    @Schema(description = "호스트 유저 id", example = "1", required = true)
    @Positive
    private Long hostUserId;



    //생성자
    public RequestRoomDto(String title, String description, CategoryEnum category, String location, LocalDateTime meetupStartDate, LocalDateTime meetupEndDate, Integer maxJoinNumber, Integer currentJoinNumber, Long price, RoomStatus roomStatus,RoomType roomType, Long viewCount, Long hostUserId, String imagePath) {
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

        this.hostUserId = hostUserId;
        this.imagePath = imagePath;
    }

}