package com.example.meetup_study.joinedUser.domain.dto;

import com.example.meetup_study.joinedUser.domain.JoinedUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class JoinedUserDto {

    @Positive
    @NotBlank(message = "id는 필수 입력 값입니다.")
    private Long id;

    @NotBlank(message = "RoomId은 필수 입력 값입니다.")
    private Long RoomId;

    @NotBlank(message = "UserId은 필수 입력 값입니다.")
    private Long UserId;

    public JoinedUserDto(Long id, Long roomId, Long userId) {
        this.id = id;
        RoomId = roomId;
        UserId = userId;
    }

    public JoinedUserDto convertToJoinedUserDto(JoinedUser joinedUser){
        return new JoinedUserDto(joinedUser.getId(), joinedUser.getRoom().getId(), joinedUser.getUser().getId());
    }
}
