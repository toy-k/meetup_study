package com.example.meetup_study.user.fakeUser;


import com.example.meetup_study.user.domain.enums.RoleType;
import com.example.meetup_study.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FakeUserDto {

    private Long id;
    private String username;
    private byte[] profile;
    private String email;
    private String description;
    //fakeuser
    private String accessToken;
    private String refreshToken;
    private RoleType roleType;


    public FakeUserDto(Long id, String username, byte[] profile, String email, String description, RoleType roleType) {
        this.id = id;
        this.username = username;
        this.profile = profile;
        this.email = email;
        this.description = description;
        this.roleType = roleType;
    }

    //fakeuser
    public FakeUserDto(Long id, String username, byte[] profile, String email, String description, RoleType roleType,String accessToken, String refreshToken) {
        this.id = id;
        this.username = username;
        this.profile = profile;
        this.email = email;
        this.description = description;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.roleType = roleType;
    }

    public FakeUserDto converToUserDto(User user) {
        return new FakeUserDto(
                user.getId(),
                user.getUsername(),
                user.getUserImage().getProfile(),
                user.getEmail(),
                user.getDescription(),
                user.getRoleType()
        );
    }
}