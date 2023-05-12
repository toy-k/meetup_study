package com.example.meetup_study.user.fakeUser;

import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.user.domain.ProviderType;
import com.example.meetup_study.user.domain.RoleType;
import com.example.meetup_study.user.domain.User;
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

    @GetMapping
    public String getFakeUser() {
        return "fakeUser";
    }

    @PostMapping
    public String createFakeUser() {
        log.debug("[FakeUserController] createFakeUser");

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

        log.debug("======\n [FakeUserController] deleteFakeUse");

        for(int i=1; i<6; i++) {

            User user = fakeUserRepository.findByUsername("fakeuser"+i).orElse(null);

            fakeUserService.deleteFakeUser(user);

        }

        return "deleteFakeUsers";
    }

    @GetMapping("/{username}")
    public ResponseEntity<FakeUserDto> readFakeUser(@PathVariable("username") String username) {

        System.out.println("======\n [FakeUserController] readFakeUse");

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
        System.out.println("======\n [FakeUserController] findAll");
        return ResponseEntity.ok(fakeUserService.findAll());
    }

}
