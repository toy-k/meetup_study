package com.example.meetup_study.room.domain;

import com.example.meetup_study.Category.domain.Category;
import com.example.meetup_study.common.domain.BaseEntity;
import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.image.roomImage.domain.RoomImage;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.enums.RoomStatus;
import com.example.meetup_study.room.domain.enums.RoomType;
import com.example.meetup_study.upload.roomUpload.domain.Upload;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    //모임 장소
    @Column(name = "location")
    private String location;

    //모임 미팅 시작 시간
    @Column(name = "meetup_start_date")
    private LocalDateTime meetupStartDate;

    //모임 미팅 끝 시간
    @Column(name = "meetup_end_date")
    private LocalDateTime meetupEndDate;

    //모임 최대 인원
    @Column(name = "max_join_number")
    private Integer maxJoinNumber;

    @Column(name = "current_join_number")
    private Integer currentJoinNumber = 1;

    @Column(name = "price")
    private Long price;

    @Column(name = "room_status")
    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus = RoomStatus.OPEN;//OPEN, FULL, CLOSE

    @Column(name = "room_type")
    @Enumerated(EnumType.STRING)
    private RoomType roomType; //Online, Offline

    @Column(name = "view_count")
    private Long viewCount = 1L;

    //모임 프로필사진
    @Column(name = "meetup_photo_path")
    private String meetupPhotoPath;

    //다른 테이블처럼 byte 형태로 수정.
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_image_id")
    private RoomImage roomImage;



    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @Column(name="upload_list")
    private List<Upload> uploadList = new ArrayList<>();

    //지양, 테스트 때문
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @Column(name="joined_user_list")
    private List<JoinedUser> joinedUserList = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @Column(name="host_user_list")
    private List<HostUser> hostUserList = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @Column(name="review_list")
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @Column(name="host_review_list")
    private List<HostReview> hostReviewList = new ArrayList<>();


    ////////////////////////////////////////////

//    //모임 주최자
//    @Column(name = "host_user_id")
//    private Long hostUserId;



    //add upload
    public void addUpload(Upload upload) {
        this.uploadList.add(upload);
    }

    //add joinedUser
    public void addJoinedUser(JoinedUser joinedUser) {
        this.joinedUserList.add(joinedUser);
    }

    //add hostUser
    public void addHostUser(HostUser hostUser) {
        this.hostUserList.add(hostUser);
    }

    public void addReview(Review review) {
        this.reviewList.add(review);
    }

    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeDescription(String description) {
        this.description = description;
    }
    public void changeMeetupStartDate(LocalDateTime meetupStartDate) {
        this.meetupStartDate = meetupStartDate;
    }
    public void changeMeetupEndDate(LocalDateTime meetupEndDate){
        this.meetupEndDate = meetupEndDate;
    }
    public void changeMeetupLocation(String meetupLocation) {
        this.location = meetupLocation;
    }
    public void changeMeetupPhotoPath(String meetupPhotoPath) {
        this.meetupPhotoPath = meetupPhotoPath;
    }
    public void changeCategory(Category category) {
        this.category = category;
    }
    public void changeMaxJoinNumber(Integer maxJoinNumber) {
        this.maxJoinNumber = maxJoinNumber;
    }

    public void changeCurrentJoinNumber(Integer currentJoinNumber) {
        this.currentJoinNumber = currentJoinNumber;
    }

    public void changePrice(Long price) {
        this.price = price;
    }

    public void changeRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public void changeRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void changeViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public void changeRoomImage(RoomImage roomImage) {
        this.roomImage = roomImage;
    }

    public Room(RequestRoomDto requestRoomDto, Category category,RoomImage roomImage) {
        this.title = requestRoomDto.getTitle();
        this.description = requestRoomDto.getDescription();
        this.category = category;
        this.location = requestRoomDto.getLocation();
        this.meetupStartDate = requestRoomDto.getMeetupStartDate();
        this.meetupEndDate = requestRoomDto.getMeetupEndDate();
        this.maxJoinNumber = requestRoomDto.getMaxJoinNumber();
        this.currentJoinNumber = requestRoomDto.getCurrentJoinNumber();
        this.price = requestRoomDto.getPrice();
        this.roomStatus = requestRoomDto.getRoomStatus();
        this.roomType = requestRoomDto.getRoomType();
        this.viewCount = requestRoomDto.getViewCount();

        this.roomImage = roomImage;

//        this.hostUserId = requestRoomDto.getHostUserId();
    }
}
