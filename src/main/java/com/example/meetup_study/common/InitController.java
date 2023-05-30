package com.example.meetup_study.common;

import com.example.meetup_study.Category.CategoryService;
import com.example.meetup_study.Category.domain.Category;
import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.room.domain.RoomStatus;
import com.example.meetup_study.room.domain.RoomType;
import com.example.meetup_study.user.domain.*;
import com.example.meetup_study.Category.domain.CategoryEnum;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.user.domain.repository.UserRepository;
import com.example.meetup_study.user.fakeUser.FakeRepository;
import com.example.meetup_study.user.fakeUser.FakeUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/init")
public class InitController {
    private final FakeUserServiceImpl fakeUserService;
    private final FakeRepository fakeUserRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final CategoryService categoryService;


    @PostMapping
    @PostConstruct //스프링 컨테이너 생성 전에 초기화돼서 joinedUser, HoustUser 생성안함
    @Transactional
    public String createDummy(){

        String username;
        String imageUrl;
        String email;
        String description;

        for(int i = 1; i<7; i++){
            username = "fakeuser"+i;
            imageUrl = "fakeuser"+i+"imageUrl";
            email = "jeonghwanlee1"+i+"@gmail.com";
            description = "fakeuser"+i+"description";


            User user = new User(username, imageUrl, email, description, RoleType.USER, ProviderType.GITHUB, "provider_id");

            fakeUserService.createFakeUser(user);
        };




        //category

        for (CategoryEnum cate : CategoryEnum.values()) {
            categoryService.createCategory(cate);
        }





        //room
        String title;
        String desc;
        LocalDateTime meetupStartDate;
        LocalDateTime meetupEndDate;
        String meetupLocation;
        String meetupPhotoPath;
        CategoryEnum category;
        Long hostUserId;
        Integer currentJoinNumber;
        Long viewCount;
        Category categorys;
        Integer maxJoinNumber;
        Long price;
        RoomStatus roomStatus;
        RoomType roomType;

        Optional<User> user = fakeUserRepository.findByUsername("fakeuser5");


        for(int i=1; i<12; i++){

            title = "title"+i;
            desc = "desc"+i;
            if(i%2==0){
                meetupStartDate = LocalDateTime.now().plusDays(2);
                meetupEndDate = LocalDateTime.now().plusDays(3);
            }else {
                meetupStartDate = LocalDateTime.now().minusDays(4);
                meetupEndDate = LocalDateTime.now().minusDays(3);
            }
            meetupLocation = "meetupLocation"+i;
            meetupPhotoPath = "meetupPhotoPath"+i;
            category = CategoryEnum.values()[i%5];
            hostUserId = user.get().getId();
            currentJoinNumber = 1;
            viewCount =1L;
            categorys = categoryService.getCategory(category).get();
            maxJoinNumber = 10;
            price = 10000L;
            roomStatus = RoomStatus.WAITING;
            roomType = RoomType.ONLINE;

            RequestRoomDto requestRoomDto = new RequestRoomDto(title, desc, category, meetupLocation, meetupStartDate, meetupEndDate, maxJoinNumber, currentJoinNumber, price, roomStatus, roomType, viewCount,meetupPhotoPath, hostUserId);

            Optional<User> userOpt = userRepository.findById(user.get().getId());
            if(userOpt.isPresent()){
                Room room = roomRepository.save(new Room(requestRoomDto, categorys));
                JoinedUser joinedUser = new JoinedUser(userOpt.get(), room);

                room.addJoinedUser(joinedUser);
                HostUser hostUser = new HostUser(userOpt.get(), room);
                room.addHostUser(hostUser);
            }else{
                throw new IllegalArgumentException("User가 없습니다.");
            }
        }
        return "createdummy";

    }
}
