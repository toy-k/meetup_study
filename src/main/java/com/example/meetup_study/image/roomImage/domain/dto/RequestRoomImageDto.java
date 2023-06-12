package com.example.meetup_study.image.roomImage.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestRoomImageDto {

    @Schema(description = "방 id", example = "1", required = true)
    @Id
    @NotNull(message = "roomId는 필수 값입니다.")
    private Long roomId;
}
