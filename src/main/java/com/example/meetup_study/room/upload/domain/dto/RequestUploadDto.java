package com.example.meetup_study.room.upload.domain.dto;


import com.example.meetup_study.room.domain.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUploadDto {

//    @NotNull(message = "room은 필수 입력 값 입니다.")
//    private Room room;

    @NotNull(message = "filename은 필수 입력 값 입니다.")
    private String fileName;

    @NotNull(message = "filePath은 필수 입력 값 입니다.")
    private String filePath;


}
