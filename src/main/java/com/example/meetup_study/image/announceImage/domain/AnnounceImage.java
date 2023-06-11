package com.example.meetup_study.image.announceImage.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class AnnounceImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announce_image_id")
    private Long id;

    @Column(name = "profile")
    private byte[] profile;

    public AnnounceImage(byte[] profile){
        this.profile = profile;
    }

    public void changeProfile(byte[] profile) {
        this.profile = profile;
    }
}
