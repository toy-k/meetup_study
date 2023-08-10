package com.example.meetup_study.image.roomImage.service;

import com.example.meetup_study.image.roomImage.domain.RoomImage;
import com.example.meetup_study.image.roomImage.domain.dto.RoomImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface RoomImageService {
    Optional<RoomImageDto> updateRoomImage(MultipartFile file, Long roomId, Long userId);
    Optional<RoomImageDto> getRoomImage(Long roomId);
    Optional<RoomImageDto> deleteRoomImage(Long roomId);
}
