package com.example.meetup_study.hostUser.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestHostUserDto {

    @Schema(description = "호스트 유저가 속한 방 id", example = "1", required = true)
    @NotNull(message = "RoomId은 필수 입력 값입니다.")
    private Long RoomId;

    @Schema(description = "호스트 유저 id", example = "1", required = true)
    @NotNull(message = "UserId은 필수 입력 값입니다.")
    private Long UserId;

}
