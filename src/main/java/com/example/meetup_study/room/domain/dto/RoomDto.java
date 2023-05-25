package com.example.meetup_study.room.domain.dto;

import com.example.meetup_study.room.domain.Category;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RoomDto {

    @Positive
    @NotBlank(message = "id는 필수 입력 값입니다.")
    private Long id;

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


    public RoomDto(Long id, String title, String description, LocalDateTime joinEndDate, LocalDateTime meetupStartDate, LocalDateTime meetupEndDate, String meetupLocation, String meetupPhotoUrl, Category category, Long hostUserId, Integer joinNumber) {

        this.id = id;
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

    public RoomDto convertToRoomDto(Room room) {
        return new RoomDto(
                room.getId(),
                room.getTitle(),
                room.getDescription(),
                room.getJoinEndDate(),
                room.getMeetupStartDate(),
                room.getMeetupEndDate(),
                room.getMeetupLocation(),
                room.getMeetupPhotoUrl(),
                room.getCategory(),
                room.getHostUserId(),
                room.getJoinNumber()
        );
    }
}

