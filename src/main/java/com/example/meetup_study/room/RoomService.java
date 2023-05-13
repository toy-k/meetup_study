package com.example.meetup_study.room;

import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    Optional<Room> createRoom(RequestRoomDto requestRoomDto);

    Optional<Room> getRoom(Long id);

    List<RoomDto> getRoomList();

    List<RoomDto> getRoomListBeforeMeetupStart();

    List<RoomDto> getRoomListAfterMeetupStart();

    Optional<RoomDto> updateRoom(RoomDto roomDto, Long userId);

    Optional<RoomDto> deleteRoom(Long id, Long userId);

    Optional<RoomDto> deleteAllRooms(Long id, Long userId);

}
