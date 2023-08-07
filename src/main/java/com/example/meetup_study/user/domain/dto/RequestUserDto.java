package com.example.meetup_study.user.domain.dto;

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
public class RequestUserDto {

    @Schema(description = "유저 이름", example = "홍길동", required = true)
    @Min(value = 3, message = "username은 3글자 이상입니다.")
    private String username;

    @Schema(description = "유저 이메일", example = "fakeUser@fake.com", required = true)
    @Email(message = "email 형식이 올바르지 않습니다.")
    @NotBlank(message = "email은 필수 입력 값입니다.")
    private String email;


    @Schema(description = "유저 소개", example = "안녕하세요", required = true)
    private String description;



//    public RequestUserDto converToRequestUserDto(User user) {
//        return new RequestUserDto(
//                user.getUsername(),
//                user.getEmail(),
//                user.getDescription()
//        );
//    }
}
