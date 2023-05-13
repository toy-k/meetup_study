package com.example.meetup_study.room.domain;

import com.example.meetup_study.common.domain.BaseEntity;
import com.example.meetup_study.joinedUser.JoinedUser;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column()
    private String title;

    @Lob
    private String description;

    //모임 주최자
    @Column(name = "host_user_id")
    private Long hostUserId;

    //모임 모집 마감일
    @Column(name = "join_end_date")
    private LocalDateTime joinEndDate;

    //모임 미팅 시작 시간
    @Column(name = "meetup_start_date")
    private LocalDateTime meetupStartDate;

    //모임 미팅 끝 시간
    @Column(name = "meetup_end_date")
    private LocalDateTime meetupEndDate;

    //모임 장소
    @Column(name = "meetup_location")
    private String meetupLocation;

    //모임 프로필사진
    @Column(name = "meetup_photo_url")
    private String meetupPhotoUrl;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "join_number")
    private Integer joinNumber;

    //지양, 테스트 때문
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @Column(name="joined_user_list")
    private List<JoinedUser> joinedUserList = new ArrayList<>();

    //add joinedUser
    public void addJoinedUser(JoinedUser joinedUser) {
        this.joinedUserList.add(joinedUser);
    }
    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeDescription(String description) {
        this.description = description;
    }
    public void changeJoinEndDate(LocalDateTime joinEndDate) {
        this.joinEndDate = joinEndDate;
    }
    public void changeMeetupStartDate(LocalDateTime meetupStartDate) {
        this.meetupStartDate = meetupStartDate;
    }
    public void changeMeetupEndDate(LocalDateTime meetupEndDate){
        this.meetupEndDate = meetupEndDate;
    }
    public void changeMeetupLocation(String meetupLocation) {
        this.meetupLocation = meetupLocation;
    }
    public void changeMeetupPhotoUrl(String meetupPhotoUrl) {
        this.meetupPhotoUrl = meetupPhotoUrl;
    }
    public void changeCategory(Category category) {
        this.category = category;
    }

    public Room(RequestRoomDto requestRoomDto) {
        this.title = requestRoomDto.getTitle();
        this.description = requestRoomDto.getDescription();
        this.hostUserId = requestRoomDto.getHostUserId();
        this.joinEndDate = requestRoomDto.getJoinEndDate();
        this.meetupStartDate = requestRoomDto.getMeetupStartDate();
        this.meetupEndDate = requestRoomDto.getMeetupEndDate();
        this.meetupLocation = requestRoomDto.getMeetupLocation();
        this.meetupPhotoUrl = requestRoomDto.getMeetupPhotoUrl();
        this.category = requestRoomDto.getCategory();
        this.joinNumber = requestRoomDto.getJoinNumber();
    }
}
