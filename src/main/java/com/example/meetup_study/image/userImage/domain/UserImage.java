package com.example.meetup_study.image.userImage.domain;

import com.example.meetup_study.common.domain.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity(name="user_image")
@Getter
@RequiredArgsConstructor
//@Table(name = "user_images")
public class UserImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_image_id")
    private Long id;

    @Column()
    private String path;

    @Lob
    @Column(name = "profile")
    private byte[] profile;


    public UserImage(String path){
        this.path = path;
    }

    public UserImage(byte[] profile){
        this.profile = profile;
    }

    public void changePath(String path) {
        this.path = path;
    }

    public void changeProfile(byte[] profile) {
        this.profile = profile;
    }
}
