package com.example.meetup_study.upload.announceUpload.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDeleteAnnounceUploadDto {

    @Schema(description = "announceId", example = "1", required = true)
    @NotNull(message = "announceId는 필수 입력 값입니다.")
    private Long announceId;

    @Schema(description = "fileName", example = "fileName", required = true)
    @NotNull(message = "fileName는 필수 입력 값입니다.")
    private String fileName;
}