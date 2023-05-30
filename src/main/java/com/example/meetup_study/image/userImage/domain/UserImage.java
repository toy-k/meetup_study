package com.example.meetup_study.image.userImage.domain;

import com.example.meetup_study.common.domain.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@RequiredArgsConstructor
public class UserImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usee_image_id")
    private Long id;

    @Column()
    private String path;

    public UserImage(String path){
        this.path = path;
    }
}
