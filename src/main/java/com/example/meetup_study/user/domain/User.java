package com.example.meetup_study.user.domain;


import com.example.meetup_study.announce.domain.Announce;
import com.example.meetup_study.common.domain.BaseEntity;
import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.image.userImage.domain.UserImage;
import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.review.domain.Review;
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

    @Column(name = "username", unique = true)
    @org.hibernate.annotations.Index(name = "idx_username")
    private String username;

    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_image_id")
    private UserImage userImage;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Column(name="host_user_list")
    private List<HostUser> hostUserList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Column(name="review_list")
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Column(name="announce_list")
    private List<Announce> announceList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Column(name="host_review_list")
    private List<HostReview> hostReviewList = new ArrayList<>();

    User(Long id, String username, UserImage userImage, String email, String description) {
        this.id = id;
        this.username = username;
        this.userImage = userImage;
        this.email = email;
        this.description = description;
    }

    public User(String username, UserImage userImage, String email, String description, RoleType roleType, ProviderType providerType, String providerId) {
        this.username = username;
        this.userImage = userImage;
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

    public void changeUserImage(UserImage userImage) {
        this.userImage = userImage;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void addAnnounce(Announce announce) {
        this.announceList.add(announce);
    }
}
