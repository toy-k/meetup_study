package com.example.meetup_study.image.roomImage.domain;

import com.example.meetup_study.common.domain.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class RoomImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_image_id")
    private Long id;

    @Column()
    private String path;

    public RoomImage(String path){
        this.path = path;
    }

    public void changePath(String path) {
        this.path = path;
    }
}
