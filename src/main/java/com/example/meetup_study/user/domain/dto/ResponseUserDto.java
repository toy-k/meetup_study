package com.example.meetup_study.user.domain.dto;

import com.example.meetup_study.user.domain.RoleType;
import com.example.meetup_study.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Email(message = "email 형식이 올바르지 않습니다.")
    @NotBlank(message = "email은 필수 입력 값입니다.")
    private String email;


    private String description;

    @Schema(description = "유저 권한", example = "USER", required = true)
    private RoleType roleType;


    public ResponseUserDto(String username, String email, String description, RoleType roleType) {
        this.username = username;
        this.email = email;
        this.description = description;
        this.roleType = roleType;
    }

    public ResponseUserDto converToReesponseUserDto(User user) {
        return new ResponseUserDto(
                user.getUsername(),
                user.getEmail(),
                user.getDescription(),
                user.getRoleType()
        );
    }
}