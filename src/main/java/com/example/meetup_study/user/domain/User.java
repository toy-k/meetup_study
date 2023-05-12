package com.example.meetup_study.user.domain;


import com.example.meetup_study.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Table(name = "users")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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



}
