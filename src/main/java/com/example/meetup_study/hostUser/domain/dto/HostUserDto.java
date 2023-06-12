package com.example.meetup_study.hostUser.domain.dto;


import com.example.meetup_study.hostUser.domain.HostUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class HostUserDto {

    @Schema(description = "HousUser row id", example = "1", required = true)
    @Positive
    @NotNull(message = "id는 필수 입력 값입니다.")
    private Long id;

    @Schema(description = "호스트 유저가 속한 방 id", example = "1", required = true)
    @NotNull(message = "RoomId은 필수 입력 값입니다.")
    private Long RoomId;

    @Schema(description = "호스트 유저 id", example = "1", required = true)
    @NotNull(message = "UserId은 필수 입력 값입니다.")
    private Long UserId;

    public HostUserDto(Long id, Long roomId, Long userId) {
        this.id = id;
        RoomId = roomId;
        UserId = userId;
    }

    public HostUserDto convertToHostUserDto(HostUser hostUser){
        return new com.example.meetup_study.hostUser.domain.dto.HostUserDto(hostUser.getId(), hostUser.getRoom().getId(), hostUser.getUser().getId());
    }
}