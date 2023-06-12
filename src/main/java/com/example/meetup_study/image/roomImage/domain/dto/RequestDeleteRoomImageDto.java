package com.example.meetup_study.image.roomImage.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class RequestDeleteRoomImageDto {

    @Schema(description = "방 id", example = "1", required = true)
    @Id
    @NotNull(message = "roomId는 null이 될 수 없습니다.")
    private Long roomId;

}
