package com.example.meetup_study.user.domain;


import com.example.meetup_study.common.domain.BaseEntity;
import com.example.meetup_study.joinedUser.JoinedUser;
import com.example.meetup_study.room.domain.Room;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Table(name = "users")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    @Column()
    private String username;

    private String password;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(unique = true)
    private String email;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private RoleType roleType; //ADMIN, USER

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type")
    private ProviderType providerType;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "refresh_token")
    private String refreshToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Column(name="joined_user_list")
    private List<JoinedUser> joinedUserList = new ArrayList<>();


    User(Long id, String username, String imageUrl, String email, String description) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.email = email;
        this.description = description;
    }

    public User(String username, String imageUrl, String email, String description, RoleType roleType, ProviderType providerType, String providerId) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.email = email;
        this.description = description;
        this.roleType = roleType;
        this.providerType = providerType;
        this.providerId = providerId;
    }

    public void updateRefreshToken(String refreshToken) {
        log.debug("[User entity] updateRefreshToken");
        this.refreshToken = refreshToken;
    }

    public void changeUsername(String username) {
        this.username = username;
    }

    public void changeImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

}
