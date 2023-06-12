package com.example.meetup_study.upload.roomUpload.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadDto {

//    @Positive
//    @NotBlank(message = "id는 필수 입력 값입니다.")
//    private Long id;

//    @NotNull(message = "room은 필수 입력 값 입니다.")
//    private Room room;

    @Schema(description = "fileName", example = "fileName", required = true)
    @NotNull(message = "filename은 필수 입력 값 입니다.")
    private String fileName;

    @Schema(description = "filePath", example = "filePath", required = true)
    @NotNull(message = "filePath은 필수 입력 값 입니다.")
    private String filePath;

//    @NotNull(message = "fileType은 필수 입력 값 입니다.")
//    private String fileType;
//
//    @NotNull(message = "filesize은 필수 입력 값 입니다.")
//    private Long fileSize;
}
