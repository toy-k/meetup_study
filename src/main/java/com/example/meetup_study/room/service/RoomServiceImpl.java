package com.example.meetup_study.room.service;

import com.example.meetup_study.category.CategoryService;
import com.example.meetup_study.category.domain.Category;
import com.example.meetup_study.category.exception.CategoryNotFoundException;
import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.image.roomImage.domain.RoomImage;
import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.mapper.RoomMapper;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.room.exception.RoomInvalidRequestException;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.repository.UserRepository;
import com.example.meetup_study.user.fakeUser.exception.UserInvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final CategoryService categoryService;
    private final RoomMapper roomMapper;

//    RoomDto roomDto = roomMapper.toRoomDto(room);


    @Transactional
    @Override
    public Optional<RoomDto> createRoom(RequestRoomDto requestRoomDto, Long userId) {

        if(userId != requestRoomDto.getHostUserId()){
            throw new RoomInvalidRequestException("이 유저는 방을 만들지 않았습니다.");
        }

        if(requestRoomDto.getMeetupEndDate().isBefore(requestRoomDto.getMeetupStartDate())){
            throw new RoomInvalidRequestException("시작 날짜가 끝나는 날짜보다 늦습니다.");
        }

        requestRoomDto.setCurrentJoinNumber(1);
        requestRoomDto.setViewCount(1L);

        Optional<User> userOpt = userRepository.findById(requestRoomDto.getHostUserId());
        Optional<Category> categoryOpt = categoryService.getCategory(requestRoomDto.getCategory());


        RoomImage roomImage = new RoomImage();

        Room room = new Room(requestRoomDto, categoryOpt.get(), roomImage);
        JoinedUser joinedUser = new JoinedUser(userOpt.get(), room);
        room.addJoinedUser(joinedUser);

        HostUser hostUser = new HostUser(userOpt.get(), room);
        room.addHostUser(hostUser);
        initializeViewCount(room.getId());

        Room roomRes = roomRepository.save(room);

        RoomDto roomDto = roomMapper.toRoomDto(roomRes);


        return Optional.ofNullable(roomDto);

    }

    @Transactional
    @Override
    public Optional<RoomDto> getRoomAndIncrementViewCount(Long id) {
        Optional<Room> roomOpt = roomRepository.findRoomWithCategoryAndHostUsersAndImage(id);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            Long viewCount = this.incrementViewCount(id);
            room.changeViewCount(viewCount);
        } else {
            throw new RoomNotFoundException();
        }

        RoomDto roomDto = roomMapper.toRoomDto(roomOpt.get());

        return Optional.ofNullable(roomDto);
    }

    @Override
    public Optional<Room> getRoom(Long id) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        if(roomOpt.isPresent()) {
            return roomOpt;
        }else{
            throw new RoomNotFoundException();
        }
    }

    @Override
    public List<RoomDto> getRoomList(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by("id").descending());
        Page<Room> roomsPage = roomRepository.findAllWithCategoryAndHostUsersAndImage(pageRequest);

        List<Room> rooms = roomsPage.getContent();
        if (rooms.isEmpty()) {
            return new ArrayList<>();
        }

        List<RoomDto> roomDtos = rooms.stream().map(r -> roomMapper.toRoomDto(r)).collect(Collectors.toList());

        return roomDtos;
    }

    @Override
    public List<RoomDto> getRoomListBeforeMeetupStart(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by("id").descending());
        LocalDateTime now = now();
        Page<Room> roomsPage = roomRepository.findByMeetupStartDateBefore(now, pageRequest);

        List<Room> rooms = roomsPage.getContent();
        if (rooms.isEmpty()) {
            return new ArrayList<>(); // 빈 리스트 반환
        }

        List<RoomDto> roomDtos = rooms.stream().map(r -> roomMapper.toRoomDto(r)).collect(Collectors.toList());

        return roomDtos;
    }

    @Override
    public List<RoomDto> getRoomListAfterMeetupStart(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by("id").descending());
        LocalDateTime now = now();
        Page<Room> roomsPage = roomRepository.findByMeetupStartDateAfter(now, pageRequest);

        List<Room> rooms = roomsPage.getContent();
        if (rooms.isEmpty()) {
            return new ArrayList<>(); // 빈 리스트 반환
        }

        List<RoomDto> roomDtos = rooms.stream().map(r -> roomMapper.toRoomDto(r)).collect(Collectors.toList());

        return roomDtos;
    }

    @Transactional
    @Override
    public Optional<RoomDto> updateRoom(RoomDto roomDto, Long userId) {

        if(userId != roomDto.getHostUserId()){
            throw new RoomInvalidRequestException("이 유저는 없거나, 방을 만들지 않았습니다.");
        }

        if(roomDto.getMeetupEndDate().isBefore(roomDto.getMeetupStartDate())){
            throw new RoomInvalidRequestException("시작 날짜가 끝나는 날짜보다 늦습니다.");
        }

        Optional <Room> roomOpt = roomRepository.findRoomWithCategoryAndHostUsersAndImage(roomDto.getId());

        if(roomOpt.isPresent() && (roomOpt.get().getHostUserList().get(0).getUser().getId().equals(userId))) {

            Optional<RoomDto> roomDtoOpt = roomOpt.map(room -> {
                room.changeTitle(roomDto.getTitle());
                room.changeDescription(roomDto.getDescription());
                room.changeMeetupStartDate(roomDto.getMeetupStartDate());
                room.changeMeetupEndDate(roomDto.getMeetupEndDate());
                room.changeMeetupLocation(roomDto.getLocation());
               room.changeMaxJoinNumber(roomDto.getMaxJoinNumber());
               room.changePrice(roomDto.getPrice());
               room.changeRoomType(roomDto.getRoomType());

               if(!roomDto.getCategory().equals(room.getCategory().getName())) {
                   Optional<Category> cate = categoryService.getCategory(roomDto.getCategory());
                   if(!cate.isPresent()){
                       throw new CategoryNotFoundException();
                   }
                   room.changeCategory(cate.get());
               }

                return roomMapper.toRoomDto(room);
            });

            return roomDtoOpt;
        }else{
            throw new RoomInvalidRequestException("해당 방이 존재하지 않거나, 방장이 아닙니다.") ;
        }
    }

    @Override
    public Boolean deleteRoom(Long roomId, Long userId) {
        Optional<Room> roomOpt = roomRepository.findRoomById(roomId);

        if(!roomOpt.isPresent()) {
            throw new RoomNotFoundException();
        }
        if(userId != roomOpt.get().getHostUserList().get(0).getUser().getId()) {
            throw new UserInvalidRequestException("이 유저는  방을 만들지 않았습니다.");
        }

        roomRepository.delete(roomOpt.get());

        return true;
    }

    @Override
    public void deleteAllRooms() {
        roomRepository.deleteAll();
    }

    @Override
    public Long getRoomCount() {
        return roomRepository.count();
    }
    private void initializeViewCount(Long roomId) {
        String key = "room:" + roomId + ":viewCount";
        redisTemplate.opsForValue().set(key, "1");
    }

    private Long incrementViewCount(Long roomId) {
        String key = "room:" + roomId + ":viewCount";
        Long count = redisTemplate.opsForValue().increment(key,1L);
        System.out.println("incrementViewCount "+ roomId +" : " + count);

        return count.longValue();
    }
}
