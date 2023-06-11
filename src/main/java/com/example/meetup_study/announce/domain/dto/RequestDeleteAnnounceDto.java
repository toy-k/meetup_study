package com.example.meetup_study.announce.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class RequestDeleteAnnounceDto {

    @Id
    @NotNull(message = "announceId는 null이 될 수 없습니다.")
    private Long announceId;
}
