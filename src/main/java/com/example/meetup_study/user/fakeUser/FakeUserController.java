package com.example.meetup_study.user.fakeUser;

import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.image.userImage.UserImageService;
import com.example.meetup_study.image.userImage.domain.UserImage;
import com.example.meetup_study.image.userImage.domain.repository.UserImageRepository;
import com.example.meetup_study.user.domain.ProviderType;
import com.example.meetup_study.user.domain.RoleType;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.fakeUser.exception.UserForbiddenException;
import com.example.meetup_study.user.fakeUser.exception.UserInvalidRequestException;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import com.example.meetup_study.user.fakeUser.exception.UserUnauthenticationedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/fakeuser")
public class FakeUserController {

    private final FakeUserServiceImpl fakeUserService;
    private final FakeRepository fakeUserRepository;
    private final JwtService jwtService;
    private final UserImageRepository userImageRepository;
    private final UserImageService userImageService;
    @GetMapping
    public String getFakeUser() {
        return "fakeUser";
    }


    @PostMapping
    public String createFakeUser() {

        String username;
        String imageUrl;
        String email;
        String description;

        for(int i = 1; i<6; i++){
            username = "fakeusers"+i;
            imageUrl = "fakeuser"+i+"imageUrl";
            email = "fakeuser"+i+"@fake.com";
            description = "fakeuser"+i+"description";

            UserImage userImage = new UserImage(imageUrl);

            User user = new User(username, userImage, email, description, RoleType.USER, ProviderType.GITHUB, "provider_id");

            fakeUserService.createFakeUser(user);
            userImageService.createUserImage(userImage.getPath(), user.getId());


        };
        return "createFakeUser";
    }
    @DeleteMapping
    public String deleteFakeUsers() {


        for(int i=1; i<6; i++) {

            User user = fakeUserRepository.findByUsername("fakeusers"+i)
                    .orElseThrow(() -> new UserNotFoundException());

            fakeUserService.deleteFakeUser(user);

        }

        return "deleteFakeUsers";
    }

    @DeleteMapping("/{username}")
    public String deleteFakeUser(@PathVariable("username") String username) {


        User user = fakeUserRepository.findByUsername(username).orElse(null);

        fakeUserService.deleteFakeUser(user);

        return "deleteFakeUser";
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

        FakeUserDto fakeUserDto = new FakeUserDto(user.getId(), user.getUsername(), user.getUserImage().getPath(), user.getEmail(), user.getDescription(), accessToken, refreshToken);


        return ResponseEntity.ok(fakeUserDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(fakeUserService.findAll());
    }


    @GetMapping("/exception/{id}")
    public String exception(@PathVariable("id") Long id) {
        if(id == 1) throw new UserNotFoundException();
        else if (id == 2) throw new UserInvalidRequestException();
        else if (id == 3) throw new UserForbiddenException();
        else if (id == 4) throw new UserUnauthenticationedException();
        else return "exception";
    }


}
