package com.example.meetup_study.room.upload;

import com.example.meetup_study.room.upload.domain.Upload;
import com.example.meetup_study.room.upload.domain.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UploadServiceImpl implements UploadService{

    private final UploadRepository uploadRepository;

    @Override
    public Optional<Upload> save(Upload upload) {
        return Optional.empty();
    }

    @Override
    public Optional<Upload> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }
}
