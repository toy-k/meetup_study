package com.example.meetup_study.user.fakeUser;


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

    public FakeUserDto(Long id, String username, byte[] profile, String email, String description) {
        this.id = id;
        this.username = username;
        this.profile = profile;
        this.email = email;
        this.description = description;
    }

    //fakeuser
    public FakeUserDto(Long id, String username, byte[] profile, String email, String description, String accessToken, String refreshToken) {
        this.id = id;
        this.username = username;
        this.profile = profile;
        this.email = email;
        this.description = description;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public FakeUserDto converToUserDto(User user) {
        return new FakeUserDto(
                user.getId(),
                user.getUsername(),
                user.getUserImage().getProfile(),
                user.getEmail(),
                user.getDescription()
        );
    }
}