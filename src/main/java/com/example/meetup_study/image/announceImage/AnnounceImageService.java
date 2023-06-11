package com.example.meetup_study.image.announceImage;

import com.example.meetup_study.image.announceImage.domain.dto.AnnounceImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface AnnounceImageService {
    Optional<AnnounceImageDto> updateAnnounceImage(MultipartFile file, Long announceId);
    Optional<AnnounceImageDto> getAnnounceImage(Long announceId);
    Optional<AnnounceImageDto> deleteAnnounceImage(Long announceId);
}
