package com.example.meetup_study.upload.announceUpload.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestAnnounceUploadDto {
    @NotNull(message = "filename은 필수 입력 값 입니다.")
    private String fileName;

    @NotNull(message = "filePath은 필수 입력 값 입니다.")
    private String filePath;

}
