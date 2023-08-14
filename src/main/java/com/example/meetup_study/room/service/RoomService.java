package com.example.meetup_study.room.service;

import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    Optional<RoomDto> createRoom(RequestRoomDto requestRoomDto, Long userId);

    Optional<RoomDto> getRoomAndIncrementViewCount(Long id);

    Optional<Room> getRoom(Long id);

    List<RoomDto> getRoomList(Integer page, Integer size);

    List<RoomDto> getRoomListBeforeMeetupStart(Integer page, Integer size);

    List<RoomDto> getRoomListAfterMeetupStart(Integer page, Integer size);

    Optional<RoomDto> updateRoom(RoomDto roomDto, Long userId);

    Boolean deleteRoom(Long roomId, Long userId);

    void deleteAllRooms();

    Long getRoomCount();

}
