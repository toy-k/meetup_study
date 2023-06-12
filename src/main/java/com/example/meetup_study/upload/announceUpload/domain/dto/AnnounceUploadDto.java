package com.example.meetup_study.upload.announceUpload.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnnounceUploadDto {


    @Schema(description = "fileName", example = "fileName", required = true)
    @NotNull(message = "filename은 필수 입력 값 입니다.")
    private String fileName;

    @Schema(description = "filePath", example = "filePath", required = true)
    @NotNull(message = "filePath은 필수 입력 값 입니다.")
    private String filePath;

}
