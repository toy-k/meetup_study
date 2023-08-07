package com.example.meetup_study.user.domain.dto;

import com.example.meetup_study.user.domain.RoleType;
import com.example.meetup_study.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseUserDto {

    @Min(value = 3, message = "username은 3글자 이상입니다.")
    private String username;

    @Email(message = "email 형식이 올바르지 않습니다.")
    @NotBlank(message = "email은 필수 입력 값입니다.")
    private String email;


    private String description;

    @Schema(description = "유저 권한", example = "USER", required = true)
    private RoleType roleType;


//    public ResponseUserDto converToReesponseUserDto(User user) {
//        return new ResponseUserDto(
//                user.getUsername(),
//                user.getEmail(),
//                user.getDescription(),
//                user.getRoleType()
//        );
//    }
}