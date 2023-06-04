package com.example.meetup_study.upload.roomUpload.domain.repository;

import com.example.meetup_study.upload.roomUpload.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadRepository extends JpaRepository<Upload, Long> {
    void deleteByFileName(String fileName);

    Optional<Upload> findByFileName(String fileName);

    Optional<Upload> findByRoomId(Long roomId);
}
