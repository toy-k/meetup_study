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

    @Column()
    private String path;

    public AnnounceImage(String path){
        this.path = path;
    }

    public void changePath(String path) {
        this.path = path;
    }
}
