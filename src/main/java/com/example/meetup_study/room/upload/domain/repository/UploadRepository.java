package com.example.meetup_study.room.upload.domain.repository;

import com.example.meetup_study.room.upload.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepository extends JpaRepository<Upload, Long> {
}
