package com.example.meetup_study.image.roomImage.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class RequestDeleteRoomImageDto {
    @Id
    @NotNull(message = "roomId는 null이 될 수 없습니다.")
    private Long roomId;

}
