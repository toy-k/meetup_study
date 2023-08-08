package com.example.meetup_study.joinedUser.domain.dto;

import com.example.meetup_study.joinedUser.domain.JoinedUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class JoinedUserDto {

    @Schema(description = "참여 유저 id", example = "1", required = true)
    @Positive
    @NotNull(message = "id는 필수 입력 값입니다.")
    private Long id;

    @Schema(description = "참여 유저가 속한 방 id", example = "1", required = true)
    @NotNull(message = "RoomId은 필수 입력 값입니다.")
    private Long RoomId;

    @Schema(description = "참여 유저 id", example = "1", required = true)
    @NotNull(message = "UserId은 필수 입력 값입니다.")
    private Long UserId;

//    public JoinedUserDto(Long id, Long roomId, Long userId) {
//        this.id = id;
//        RoomId = roomId;
//        UserId = userId;
//    }
//
//    public JoinedUserDto convertToJoinedUserDto(JoinedUser joinedUser){
//        return new JoinedUserDto(joinedUser.getId(), joinedUser.getRoom().getId(), joinedUser.getUser().getId());
//    }
}
