package com.example.meetup_study.image.userImage.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserImageDto {

    @Schema(description = "유저 이미지", example = "file.png", required = true)
    private byte[] profile;

}
