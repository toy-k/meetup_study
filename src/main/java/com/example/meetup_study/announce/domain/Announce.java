package com.example.meetup_study.announce.domain;

import com.example.meetup_study.common.domain.BaseEntity;
import com.example.meetup_study.image.announceImage.domain.AnnounceImage;
import com.example.meetup_study.upload.announceUpload.domain.AnnounceUpload;
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

    @OneToMany(mappedBy = "announce", cascade = CascadeType.ALL)
    @Column(name="announce_upload_list")
    private List<AnnounceUpload> announceUploadList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "announce_image_id")
    private AnnounceImage announceImage;

    @Column(name = "view_count")
    private Long viewCount = 1L;


    public void addUpload(AnnounceUpload announceUpload) {
        this.announceUploadList.add(announceUpload);
    }
    public void changeTitle(String title) {
    this.title = title;
}
    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeAnnounceImage(AnnounceImage announceImage) {
        this.announceImage = announceImage;
    }

    public void changeViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Announce(String title, String description, User user, AnnounceImage announceImage) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.announceImage = announceImage;
    }

}
