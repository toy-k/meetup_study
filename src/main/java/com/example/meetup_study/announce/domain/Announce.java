package com.example.meetup_study.announce.domain;

import com.example.meetup_study.common.domain.BaseEntity;
import com.example.meetup_study.image.announceImage.domain.AnnounceImage;
import com.example.meetup_study.image.userImage.domain.UserImage;
import com.example.meetup_study.room.upload.domain.Upload;
import com.example.meetup_study.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Announce extends BaseEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column()
    private String title;

    @Lob
    private String description;

    //announce upload 로 수정
//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
//    @Column(name="announce_upload_list")
//    private List<Upload> announceUploadList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "announce_image_id")
    private AnnounceImage announceImage;


    //    public void addUpload(Upload upload) {
//        this.announceUploadList.add(upload);
//    }
    public void changeTitle(String title) {
    this.title = title;
}
    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeAnnounceImage(AnnounceImage announceImage) {
        this.announceImage = announceImage;
    }

    public Announce(String title, String description, User user, AnnounceImage announceImage) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.announceImage = announceImage;
    }

}