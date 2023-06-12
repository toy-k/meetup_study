package com.example.meetup_study.upload.roomUpload.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUploadDto {


    @Schema(description = "roomId", example = "1", required = true)
    @Id
    @NotNull(message = "roomId는 필수 입력 값입니다.")
    private Long roomId;

}
