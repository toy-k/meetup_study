package com.example.meetup_study.upload.roomUpload.domain.repository;

import com.example.meetup_study.upload.roomUpload.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepository extends JpaRepository<Upload, Long> {
    void deleteByFileName(String fileName);
}
