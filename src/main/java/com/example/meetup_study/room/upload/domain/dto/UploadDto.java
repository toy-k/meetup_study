package com.example.meetup_study.room.upload.domain.dto;

import com.example.meetup_study.room.domain.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
