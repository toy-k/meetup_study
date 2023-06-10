package com.example.meetup_study.joinedUser.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class RequestJoinedUserDto {

    @NotNull(message = "RoomId은 필수 입력 값입니다.")
    private Long RoomId;

    @NotNull(message = "UserId은 필수 입력 값입니다.")
    private Long UserId;


    public RequestJoinedUserDto(Long roomId, Long userId) {
        RoomId = roomId;
        UserId = userId;
    }
}
