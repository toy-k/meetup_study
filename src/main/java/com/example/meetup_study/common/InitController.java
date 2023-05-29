package com.example.meetup_study.common;

import com.example.meetup_study.room.upload.domain.dto.JoinedUser;
import com.example.meetup_study.room.domain.Category;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.user.domain.ProviderType;
import com.example.meetup_study.user.domain.RoleType;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.repository.UserRepository;
import com.example.meetup_study.user.fakeUser.FakeRepository;
import com.example.meetup_study.user.fakeUser.FakeUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
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

    @PostMapping
    @PostConstruct
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

        //room
        String title;
        String desc;
        LocalDateTime joinEndDate;
        LocalDateTime meetupStartDate;
        LocalDateTime meetupEndDate;
        String meetupLocation;
        String meetupPhotoUrl;
        Category category;
        Long hostUserId;
        Integer joinNumber;
        Long viewCount;

        Optional<User> user = fakeUserRepository.findByUsername("fakeuser5");


        for(int i=1; i<12; i++){

            title = "title"+i;
            desc = "desc"+i;
            if(i%2==0){
                joinEndDate = LocalDateTime.now().plusDays(1);
                meetupStartDate = LocalDateTime.now().plusDays(2);
                meetupEndDate = LocalDateTime.now().plusDays(3);
            }else {
                joinEndDate = LocalDateTime.now().minusDays(5);
                meetupStartDate = LocalDateTime.now().minusDays(4);
                meetupEndDate = LocalDateTime.now().minusDays(3);
            }
            meetupLocation = "meetupLocation"+i;
            meetupPhotoUrl = "meetupPhotoUrl"+i;
            category = Category.values()[i%5];
            hostUserId = user.get().getId();
            joinNumber = 1;
            viewCount =1L;



            RequestRoomDto requestRoomDto = new RequestRoomDto(title, desc, joinEndDate, meetupStartDate, meetupEndDate, meetupLocation, meetupPhotoUrl, category, hostUserId, joinNumber, viewCount);

            Room room = roomRepository.save(new Room(requestRoomDto));
            Optional<User> userOpt = userRepository.findById(user.get().getId());
            if(userOpt.isPresent()){
                JoinedUser joinedUser = new JoinedUser(userOpt.get(), room);
                room.addJoinedUser(joinedUser);
            }else{
                throw new IllegalArgumentException("User가 없습니다.");
            }
        }
        return "createdummy";

    }
}
