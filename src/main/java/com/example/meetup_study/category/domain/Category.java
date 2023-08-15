package com.example.meetup_study.category.domain;

import com.example.meetup_study.common.domain.BaseEntity;
import com.example.meetup_study.room.domain.Room;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private CategoryEnum name;
    //STUDY, TRAVEL, CODING, FOOD, GAME, CAFE, ALCHOLE, ACTIVITY, CULTURE, SPORTS, ETC


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @Column(name="room_list")
    private List<Room> roomList = new ArrayList<>();

    public Category(CategoryEnum name) {
        this.name = name;
    }


}
