package com.example.meetup_study.user.fakeUser;

import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.joinedUser.JoinedUser;
import com.example.meetup_study.room.domain.Category;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.user.domain.ProviderType;
import com.example.meetup_study.user.domain.RoleType;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/fakeuser")
public class FakeUserController {

    private final FakeUserServiceImpl fakeUserService;
    private final FakeRepository fakeUserRepository;
    private final JwtService jwtService;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @GetMapping
    public String getFakeUser() {
        return "fakeUser";
    }


    @PostMapping("/dummy")
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


            RequestRoomDto requestRoomDto = new RequestRoomDto(title, desc, joinEndDate, meetupStartDate, meetupEndDate, meetupLocation, meetupPhotoUrl, category, hostUserId, joinNumber);

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

    @PostMapping
    public String createFakeUser() {

        String username;
        String imageUrl;
        String email;
        String description;

        for(int i = 1; i<6; i++){
            username = "fakeuser"+i;
            imageUrl = "fakeuser"+i+"imageUrl";
            email = "fakeuser"+i+"@fake.com";
            description = "fakeuser"+i+"description";


            User user = new User(username, imageUrl, email, description, RoleType.USER, ProviderType.GITHUB, "provider_id");

            fakeUserService.createFakeUser(user);


        };
        return "createFakeUser";
    }
    @DeleteMapping
    public String deleteFakeUsers() {


        for(int i=1; i<6; i++) {

            User user = fakeUserRepository.findByUsername("fakeuser"+i).orElse(null);

            fakeUserService.deleteFakeUser(user);

        }

        return "deleteFakeUsers";
    }

    @GetMapping("/{username}")
    public ResponseEntity<FakeUserDto> readFakeUser(@PathVariable("username") String username) {


        User user = fakeUserRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getId());

        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getId());

        fakeUserService.updateRefreshToken(user, refreshToken);

        FakeUserDto fakeUserDto = new FakeUserDto(user.getId(), user.getUsername(), user.getImageUrl(), user.getEmail(), user.getDescription(), accessToken, refreshToken);


        return ResponseEntity.ok(fakeUserDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(fakeUserService.findAll());
    }

}
