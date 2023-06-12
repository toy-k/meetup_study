package com.example.meetup_study.upload.announceUpload.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestAnnounceUploadDto {

    @Id
    @NotNull(message = "announceId는 필수 입력 값입니다.")
    private Long announceId;


}
