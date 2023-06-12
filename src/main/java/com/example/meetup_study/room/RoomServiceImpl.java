package com.example.meetup_study.room;

import com.example.meetup_study.Category.CategoryService;
import com.example.meetup_study.Category.domain.Category;
import com.example.meetup_study.Category.domain.CategoryRepository;
import com.example.meetup_study.Category.exception.CategoryNotFoundException;
import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.image.roomImage.domain.RoomImage;
import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.room.exception.RoomInvalidRequestException;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final CategoryService categoryService;

    @Transactional
    @Override
    public Optional<RoomDto> createRoom(RequestRoomDto requestRoomDto) {
        Optional<User> userOpt = userRepository.findById(requestRoomDto.getHostUserId());
        Optional<Category> categoryOpt = categoryService.getCategory(requestRoomDto.getCategory());

        if(!categoryOpt.isPresent()){
            throw new CategoryNotFoundException();
        }

         RoomImage roomImage = new RoomImage();

        Room room = roomRepository.save(new Room(requestRoomDto, categoryOpt.get(), roomImage));
        JoinedUser joinedUser = new JoinedUser(userOpt.get(), room);
        room.addJoinedUser(joinedUser);
        HostUser hostUser = new HostUser(userOpt.get(), room);
        room.addHostUser(hostUser);

        return Optional.ofNullable(new RoomDto().convertToRoomDto(room));

//        return Optional.ofNullable(room);

    }

    @Transactional
    @Override
    public Optional<Room> getRoom(Long id) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        if(roomOpt.isPresent()) {
            Room room = roomOpt.get();
            Long viewCount = this.incrementViewCount(id);
            room.changeViewCount(viewCount);
        }else{
            throw new RoomNotFoundException();
        }

        return roomOpt;
    }

    @Override
    public List<RoomDto> getRoomList(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<Room> roomsPage = roomRepository.findAll(pageRequest);

        List<Room> rooms = roomsPage.getContent();
        if (rooms.isEmpty()) {
            return new ArrayList<>(); // 빈 리스트 반환
        }

        List<RoomDto> roomDtos = rooms.stream().map(r -> new RoomDto().convertToRoomDto(r)).collect(Collectors.toList());

        return roomDtos;
    }

    @Override
    public List<RoomDto> getRoomListBeforeMeetupStart(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page-1, size);
        LocalDateTime now = now();
        Page<Room> roomsPage = roomRepository.findByMeetupStartDateBefore(now, pageRequest);

        List<Room> rooms = roomsPage.getContent();
        if (rooms.isEmpty()) {
            return new ArrayList<>(); // 빈 리스트 반환
        }

        List<RoomDto> roomDtos = rooms.stream().map(r -> new RoomDto().convertToRoomDto(r)).collect(Collectors.toList());

        return roomDtos;
    }

    @Override
    public List<RoomDto> getRoomListAfterMeetupStart(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page-1, size);
        LocalDateTime now = now();
        Page<Room> roomsPage = roomRepository.findByMeetupStartDateAfter(now, pageRequest);

        List<Room> rooms = roomsPage.getContent();
        if (rooms.isEmpty()) {
            return new ArrayList<>(); // 빈 리스트 반환
        }

        List<RoomDto> roomDtos = rooms.stream().map(r -> new RoomDto().convertToRoomDto(r)).collect(Collectors.toList());

        return roomDtos;
    }

    @Override
    public Optional<RoomDto> updateRoom(RoomDto roomDto, Long userId) {
        Optional <Room> roomOpt = roomRepository.findById(roomDto.getId());

        if(roomOpt.isPresent() && (roomOpt.get().getHostUserList().get(0).getId().equals(userId))) {

            Optional<RoomDto> roomDtoOpt = roomOpt.map(room -> {
                room.changeTitle(roomDto.getTitle());
                room.changeDescription(roomDto.getDescription());
                room.changeMeetupStartDate(roomDto.getMeetupStartDate());
                room.changeMeetupEndDate(roomDto.getMeetupEndDate());
                room.changeMeetupLocation(roomDto.getLocation());
               room.changeMaxJoinNumber(roomDto.getMaxJoinNumber());
               room.changePrice(roomDto.getPrice());
               room.changeRoomType(roomDto.getRoomType());

                Optional<Category> cate = categoryService.getCategory(roomDto.getCategory());
                if(!cate.isPresent()){
                    throw new CategoryNotFoundException();
                }
                room.changeCategory(cate.get());

                return new RoomDto().convertToRoomDto(room);
            });

            roomRepository.save(roomOpt.get());
            return roomDtoOpt;
        }else{
            throw new RoomInvalidRequestException("해당 방이 존재하지 않거나, 방장이 아닙니다.") ;
        }
    }

    @Override
    public Optional<RoomDto> deleteRoom(Long id, Long userId) {
        Optional <Room> room = roomRepository.findById(id);
        roomRepository.delete(room.get());
        Optional<RoomDto> roomDto = room.map(r -> new RoomDto().convertToRoomDto(r));

        return roomDto;
    }

    @Override
    public void deleteAllRooms() {
        roomRepository.deleteAll();
    }

    private Long incrementViewCount(Long roomId) {
        String key = "room:" + roomId + ":viewCount";
        Long count = redisTemplate.opsForValue().increment(key);
        return count.longValue();
    }
}
