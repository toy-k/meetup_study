package com.example.meetup_study.room;

import com.example.meetup_study.Category.CategoryService;
import com.example.meetup_study.Category.domain.Category;
import com.example.meetup_study.Category.domain.CategoryRepository;
import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.image.roomImage.domain.RoomImage;
import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.room.domain.repository.RoomRepository;
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
    public Optional<Room> createRoom(RequestRoomDto requestRoomDto) {
        Optional<User> userOpt = userRepository.findById(requestRoomDto.getHostUserId());
        Optional<Category> categoryOpt = categoryService.getCategory(requestRoomDto.getCategory());

        if(!categoryOpt.isPresent()){
            throw new IllegalArgumentException("category가 없습니다.");
        }

         RoomImage roomImage = new RoomImage(requestRoomDto.getImagePath());

        Room room = roomRepository.save(new Room(requestRoomDto, categoryOpt.get(), roomImage));
        JoinedUser joinedUser = new JoinedUser(userOpt.get(), room);
        room.addJoinedUser(joinedUser);
        HostUser hostUser = new HostUser(userOpt.get(), room);
        room.addHostUser(hostUser);

        return Optional.ofNullable(room);

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
            throw new IllegalArgumentException("방이 없습니다.");
        }

        return roomOpt;
    }

    @Override
    public List<RoomDto> getRoomList(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Room> roomsPage = roomRepository.findAll(pageRequest);

        List<Room> rooms = roomsPage.getContent();

        List<RoomDto> roomDtos = rooms.stream().map(r -> new RoomDto().convertToRoomDto(r)).collect(Collectors.toList());

        return roomDtos;
    }

    @Override
    public List<RoomDto> getRoomListBeforeMeetupStart(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        LocalDateTime now = now();
        Page<Room> roomsPage = roomRepository.findByMeetupStartDateBefore(now, pageRequest);

        List<Room> rooms = roomsPage.getContent();

        List<RoomDto> roomDtos = rooms.stream().map(r -> new RoomDto().convertToRoomDto(r)).collect(Collectors.toList());

        return roomDtos;
    }

    @Override
    public List<RoomDto> getRoomListAfterMeetupStart(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        LocalDateTime now = now();
        Page<Room> roomsPage = roomRepository.findByMeetupStartDateAfter(now, pageRequest);

        List<Room> rooms = roomsPage.getContent();

        List<RoomDto> roomDtos = rooms.stream().map(r -> new RoomDto().convertToRoomDto(r)).collect(Collectors.toList());

        return roomDtos;
    }

    @Override
    public Optional<RoomDto> updateRoom(RoomDto roomDto, Long userId) {
        Optional <Room> roomOpt = roomRepository.findById(roomDto.getId());

        if(roomOpt.isPresent() && (roomOpt.get().getHostUserList().get(0).getId().equals(userId))) {

            Optional<RoomDto> roomDtoOpt = roomOpt.map(room -> {
                if (roomDto.getTitle() != null) room.changeTitle(roomDto.getTitle());
                if (roomDto.getDescription() != null) room.changeDescription(roomDto.getDescription());
                if (roomDto.getMeetupStartDate() != null) room.changeMeetupStartDate(roomDto.getMeetupStartDate());
                if (roomDto.getMeetupEndDate() != null) room.changeMeetupEndDate(roomDto.getMeetupEndDate());
                if (roomDto.getLocation() != null) room.changeMeetupLocation(roomDto.getLocation());
                if (roomDto.getMeetupPhotoPath() != null) room.changeMeetupPhotoPath(roomDto.getMeetupPhotoPath());
                if (roomDto.getCategory() != null) room.changeCategory(roomDto.getCategory());
                if (roomDto.getMaxJoinNumber() != null) room.changeMaxJoinNumber(roomDto.getMaxJoinNumber());
                if (roomDto.getPrice() != null) room.changePrice(roomDto.getPrice());
                if (roomDto.getRoomType() != null) room.changeRoomType(roomDto.getRoomType());
                return new RoomDto().convertToRoomDto(room);
            });

            return roomDtoOpt;
        }else{
            throw new IllegalArgumentException("해당 방이 존재하지 않거나, 방장이 아닙니다.") ;
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
