package com.example.meetup_study.image.roomImage;

import com.example.meetup_study.image.roomImage.domain.RoomImage;

import java.util.Optional;

public interface RoomImageService {
    Optional<RoomImage> createRoomImage(String path, Long roomId);
    Optional<RoomImage> getRoomImage(Long roomId);
    Optional<RoomImage> updateRoomImage(String path, Long roomId);
    Optional<RoomImage> deleteRoomImage(Long roomId);
}
