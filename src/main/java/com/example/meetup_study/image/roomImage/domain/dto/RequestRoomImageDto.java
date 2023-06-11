package com.example.meetup_study.image.roomImage.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestRoomImageDto {

    @Id
    @NotNull(message = "roomId는 필수 값입니다.")
    private Long roomId;
}
