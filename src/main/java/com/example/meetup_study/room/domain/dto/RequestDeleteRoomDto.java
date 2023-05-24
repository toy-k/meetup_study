package com.example.meetup_study.room.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
public class RequestDeleteRoomDto {

    @Positive
    @NotBlank(message = "id는 필수 입력 값입니다.")
    private Long id;

}