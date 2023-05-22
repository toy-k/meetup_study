package com.example.meetup_study.room.upload;

import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.upload.domain.Upload;
import com.example.meetup_study.room.upload.domain.dto.FileDeleteStatus;
import com.example.meetup_study.room.upload.domain.dto.UploadDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Optional;

public interface UploadService {
    List<UploadDto> save(List<MultipartFile> files, Long roomId);
    void downloadZip(HttpServletResponse res, List<String> fileNames, Long roomId);
    FileDeleteStatus deleteByName(String fileName, Long roomId);

}
