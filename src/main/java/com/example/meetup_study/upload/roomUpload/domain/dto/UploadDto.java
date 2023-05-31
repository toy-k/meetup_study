package com.example.meetup_study.upload.roomUpload.domain.dto;

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

    @NotNull(message = "filename은 필수 입력 값 입니다.")
    private String fileName;

    @NotNull(message = "filePath은 필수 입력 값 입니다.")
    private String filePath;

//    @NotNull(message = "fileType은 필수 입력 값 입니다.")
//    private String fileType;
//
//    @NotNull(message = "filesize은 필수 입력 값 입니다.")
//    private Long fileSize;
}
