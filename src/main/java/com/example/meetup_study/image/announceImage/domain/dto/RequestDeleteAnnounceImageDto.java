package com.example.meetup_study.image.announceImage.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDeleteAnnounceImageDto {

    @Schema(description = "공지사항 id", example = "1", required = true)
    @Id
    @NotNull(message = "announceId는 필수 값입니다.")
    private Long announceId;
}
