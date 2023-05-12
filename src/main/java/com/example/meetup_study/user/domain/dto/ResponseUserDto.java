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
public class ResponseUserDto {

    @Min(value = 3, message = "username은 3글자 이상입니다.")
    private String username;

    private String imageUrl;

    @Email(message = "email 형식이 올바르지 않습니다.")
    @NotBlank(message = "email은 필수 입력 값입니다.")
    private String email;


    private String description;


    public ResponseUserDto(String username, String imageUrl, String email, String description) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.email = email;
        this.description = description;
    }

    public ResponseUserDto converToReesponseUserDto(User user) {
        return new ResponseUserDto(
                user.getUsername(),
                user.getImageUrl(),
                user.getEmail(),
                user.getDescription()
        );
    }
}