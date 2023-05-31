package com.example.meetup_study.image.announceImage;

import com.example.meetup_study.image.announceImage.domain.AnnounceImage;

import java.util.Optional;

public interface AnnounceImageService {
    Optional<AnnounceImage> createAnnounceImage(String path, Long announceId);
    Optional<AnnounceImage> getAnnounceImage(Long announceId);
    Optional<AnnounceImage> updateAnnounceImage(String path, Long announceId);
    Optional<AnnounceImage> deleteAnnounceImage(Long announceId);
}
