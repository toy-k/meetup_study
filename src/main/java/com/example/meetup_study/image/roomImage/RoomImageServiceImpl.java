package com.example.meetup_study.image.roomImage;

import com.example.meetup_study.image.roomImage.domain.RoomImage;
import com.example.meetup_study.image.roomImage.domain.repository.RoomImageRepository;
import com.example.meetup_study.room.RoomService;
import com.example.meetup_study.room.domain.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomImageServiceImpl implements RoomImageService{

    private final RoomImageRepository roomImageRepository;
    private final RoomService roomService;

    @Override
    public Optional<RoomImage> createRoomImage(String path, Long roomId) {
        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 방입니다.");

        RoomImage roomImage = new RoomImage(path);
        roomOpt.get().changeRoomImage(roomImage);

        return Optional.of(roomImageRepository.save(roomImage));
    }

    @Override
    public Optional<RoomImage> getRoomImage(Long roomId) {
        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 방입니다.");

        return Optional.ofNullable(roomOpt.get().getRoomImage());
    }

    @Override
    public Optional<RoomImage> updateRoomImage(String path, Long roomId) {
        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 방입니다.");

        RoomImage roomImage = roomOpt.get().getRoomImage();

        roomImage.changePath(path);

        return Optional.of(roomImageRepository.save(roomImage));
    }

    @Override
    public Optional<RoomImage> deleteRoomImage(Long roomId) {
        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()) throw new IllegalArgumentException("존재하지 않는 방입니다.");

        RoomImage roomImage = roomOpt.get().getRoomImage();

        roomImageRepository.delete(roomImage);

        return Optional.of(roomImage);
    }
}
