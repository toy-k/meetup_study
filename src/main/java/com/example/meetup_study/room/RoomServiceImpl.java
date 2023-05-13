package com.example.meetup_study.room;

import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;

    @Override
    public Optional<Room> createRoom(RequestRoomDto requestRoomDto) {
        Room room = roomRepository.save(new Room(requestRoomDto));
        return Optional.ofNullable(room);
    }

    @Override
    public Optional<Room> getRoom(Long id) {
        return roomRepository.findById(id);
    }

    @Override
    public List<RoomDto> getRoomList() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomDto> roomDtos = rooms.stream().map(r -> new RoomDto().convertToRoomDto(r)).collect(Collectors.toList());

        return roomDtos;
    }

    @Override
    public List<RoomDto> getRoomListBeforeMeetupStart() {
        LocalDateTime now = now();
        List<Room> rooms = roomRepository.findByMeetupStartDateBefore(now);

        List<RoomDto> roomDtos = rooms.stream().map(r -> new RoomDto().convertToRoomDto(r)).collect(Collectors.toList());

        return roomDtos;
    }

    @Override
    public List<RoomDto> getRoomListAfterMeetupStart() {
        LocalDateTime now = now();
        List<Room> rooms = roomRepository.findByMeetupStartDateAfter(now);

        List<RoomDto> roomDtos = rooms.stream().map(r -> new RoomDto().convertToRoomDto(r)).collect(Collectors.toList());

        return roomDtos;
    }

    @Override
    public Optional<RoomDto> updateRoom(RoomDto roomDto, Long userId) {
        Optional <Room> roomOpt = roomRepository.findById(roomDto.getId());

        if(roomOpt.isPresent() && (roomOpt.get().getHostUserId().equals(userId))) {

            Optional<RoomDto> roomDtoOpt = roomOpt.map(room -> {
                if (roomDto.getTitle() != null) room.changeTitle(roomDto.getTitle());
                if (roomDto.getDescription() != null) room.changeDescription(roomDto.getDescription());
                if (roomDto.getJoinEndDate() != null) room.changeJoinEndDate(roomDto.getJoinEndDate());
                if (roomDto.getMeetupStartDate() != null) room.changeMeetupStartDate(roomDto.getMeetupStartDate());
                if (roomDto.getMeetupEndDate() != null) room.changeMeetupEndDate(roomDto.getMeetupEndDate());
                if (roomDto.getMeetupLocation() != null) room.changeMeetupLocation(roomDto.getMeetupLocation());
                if (roomDto.getMeetupPhotoUrl() != null) room.changeMeetupPhotoUrl(roomDto.getMeetupPhotoUrl());
                if (roomDto.getCategory() != null) room.changeCategory(roomDto.getCategory());
                return new RoomDto().convertToRoomDto(room);
            });

            return roomDtoOpt;
        }else{
            throw new IllegalArgumentException("해당 방이 존재하지 않거나, 방장이 아닙니다.") ;
        }
    }

    @Override
    public Optional<RoomDto> deleteRoom(Long id, Long userId) {
        return Optional.empty();
    }

    @Override
    public Optional<RoomDto> deleteAllRooms(Long id, Long userId) {
        Optional <Room> room = roomRepository.findById(id);
        if(room.isPresent() && room.get().getHostUserId().equals(userId)) {
            roomRepository.delete(room.get());
            Optional<RoomDto> roomDto = room.map(r -> new RoomDto().convertToRoomDto(r));
            return roomDto;
        }else{
            throw new IllegalArgumentException("해당 방이 존재하지 않거나, 방장이 아닙니다.");
        }
    }

    public void deleteAllRooms() {
        roomRepository.deleteAll();
    }

}
