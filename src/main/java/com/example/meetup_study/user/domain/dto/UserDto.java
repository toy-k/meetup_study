package com.example.meetup_study.user.domain.dto;

import com.example.meetup_study.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    @NotBlank(message = "id는 필수 입력 값입니다.")
    private Long id;

    @Min(value = 3, message = "username은 3글자 이상입니다.")
    private String username;

    private String imageUrl;

    @Email(message = "email 형식이 올바르지 않습니다.")
    @NotBlank(message = "email은 필수 입력 값입니다.")
    private String email;


    private String description;


    public UserDto(Long id, String username, String imageUrl, String email, String description) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.email = email;
        this.description = description;
    }

    public UserDto converToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getImageUrl(),
                user.getEmail(),
                user.getDescription()
        );
    }
}