package com.example.meetup_study.room.domain.dto;

import com.example.meetup_study.Category.domain.Category;
import com.example.meetup_study.Category.domain.CategoryEnum;
import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.RoomStatus;
import com.example.meetup_study.room.domain.RoomType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomDto {

    @Positive
    @NotNull(message = "id는 필수 입력 값입니다.")
    private Long id;

    @NotBlank(message = "title은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "description은 필수 입력 값입니다.")
    private String description;

    @NotNull(message = "category은 필수 입력 값입니다.")
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

    @NotNull(message = "roomStatus은 필수 입력 값입니다.")
    private RoomStatus roomStatus;

    @NotNull(message = "roomType은 필수 입력 값입니다.")
    private RoomType roomType; //Online, Offline

    private String meetupPhotoPath;

    @Positive
    private Long viewCount;

    ///////////////////////////


    @Positive
    private Long hostUserId;




    public RoomDto(Long id, String title, String description, CategoryEnum category, String location, LocalDateTime meetupStartDate, LocalDateTime meetupEndDate, Integer maxJoinNumber, Integer currentJoinNumber, Long price, RoomStatus roomstatus, RoomType roomType, Long viewCount, String meetupPhotoPath, Long hostUserId) {

        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.location = location;
        this.meetupStartDate = meetupStartDate;
        this.meetupEndDate = meetupEndDate;
        this.maxJoinNumber = maxJoinNumber;
        this.currentJoinNumber = currentJoinNumber;
        this.price = price;
        this.roomStatus = roomstatus;
        this.roomType = roomType;
        this.viewCount = viewCount;
        this.meetupPhotoPath = meetupPhotoPath;

        this.hostUserId = hostUserId;
    }

    public RoomDto convertToRoomDto(Room room) {
        return new RoomDto(
                room.getId(),
                room.getTitle(),
                room.getDescription(),
                convertToCategoryEnum(room.getCategory()),
                room.getLocation(),
                room.getMeetupStartDate(),
                room.getMeetupEndDate(),
                room.getMaxJoinNumber(),
                room.getCurrentJoinNumber(),
                room.getPrice(),
                room.getRoomStatus(),
                room.getRoomType(),
                room.getViewCount(),
                room.getMeetupPhotoPath(),

                convertToHostUserId(room)
//                room.getHostUserList().get(0).getId()
        );
    }

    private CategoryEnum convertToCategoryEnum(Category category) {
        return category.getName();
    }

    private Long convertToHostUserId(Room room) {

        List<HostUser> hostUserList = room.getHostUserList();

        Long hostUserId = hostUserList.isEmpty() ? null : hostUserList.get(0).getUser().getId();


        return hostUserId;
    }
}

