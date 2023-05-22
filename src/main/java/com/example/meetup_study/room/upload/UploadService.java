package com.example.meetup_study.room.upload;

import com.example.meetup_study.room.upload.domain.Upload;

import java.util.Optional;

public interface UploadService {
    Optional<Upload> save(Upload upload);
    Optional<Upload> findById(Long id);
    void deleteById(Long id);

}
