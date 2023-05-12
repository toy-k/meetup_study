package com.example.meetup_study.user.fakeUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/fakeuser")
public class FakeUserController {

    @GetMapping
    public String getFakeUser() {
        return "fakeUser";
    }

}
