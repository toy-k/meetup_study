package com.example.meetup_study.upload.roomUpload;

import com.example.meetup_study.upload.FileDeleteStatus;
import com.example.meetup_study.upload.roomUpload.domain.dto.UploadDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UploadService {
    List<UploadDto> save(List<MultipartFile> files, Long roomId);
    void downloadZip(HttpServletResponse res, List<String> fileNames, Long roomId);
    FileDeleteStatus deleteByName(String fileName, Long roomId);

}