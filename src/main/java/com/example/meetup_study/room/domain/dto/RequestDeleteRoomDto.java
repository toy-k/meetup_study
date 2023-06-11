package com.example.meetup_study.room.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class RequestDeleteRoomDto {

    @Positive
    @NotNull(message = "id는 필수 입력 값입니다.")
    private Long id;

}