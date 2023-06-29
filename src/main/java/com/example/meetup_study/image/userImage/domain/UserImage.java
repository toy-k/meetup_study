package com.example.meetup_study.image.userImage.domain;

import com.example.meetup_study.common.domain.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity(name="user_image")
@Getter
@RequiredArgsConstructor
public class UserImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_image_id")
    private Long id;

    @Column(name = "profile", columnDefinition = "BLOB")
    private byte[] profile;


    public UserImage(byte[] profile){
        this.profile = profile;
    }

    public void changeProfile(byte[] profile) {
        this.profile = profile;
    }
}
