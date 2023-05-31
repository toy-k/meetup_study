package com.example.meetup_study.upload.announceUpload.domain.repository;

import com.example.meetup_study.upload.announceUpload.domain.AnnounceUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnounceUploadRepository extends JpaRepository<AnnounceUpload, Long> {
    void deleteByFileName(String s);
}
