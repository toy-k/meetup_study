package com.example.meetup_study.hostUser.domain;

import com.example.meetup_study.common.domain.BaseEntity;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.user.domain.User;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class HostUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "host_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    public HostUser() {

    }
    public HostUser(User user, Room room) {
        this.room = room;
        this.user = user;
    }

}
