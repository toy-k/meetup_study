package com.example.meetup_study.hostUser.domain.dto;


import com.example.meetup_study.hostUser.domain.HostUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class HostUserDto {

    @Positive
    @NotBlank(message = "id는 필수 입력 값입니다.")
    private Long id;

    @NotBlank(message = "RoomId은 필수 입력 값입니다.")
    private Long RoomId;

    @NotBlank(message = "UserId은 필수 입력 값입니다.")
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