package com.example.meetup_study.upload.announceUpload;

import com.example.meetup_study.upload.announceUpload.domain.AnnounceUpload;
import com.example.meetup_study.upload.announceUpload.domain.dto.AnnounceUploadDto;
import com.example.meetup_study.upload.FileDeleteStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public interface AnnounceUploadService {
    List<AnnounceUploadDto> fileUpload(List<MultipartFile> files, Long announceId);

    Optional<AnnounceUploadDto> findByAnnounceId(Long announceId);

    void downloadZip(HttpServletResponse res, List<String> fileNames, Long announceId);
    FileDeleteStatus deleteByName(String fileName, Long announceId);
}
