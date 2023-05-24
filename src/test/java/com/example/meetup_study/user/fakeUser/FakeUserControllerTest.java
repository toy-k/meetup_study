package com.example.meetup_study.user.fakeUser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@DisplayName("FakeUserController")
class FakeUserControllerTest {

    @Autowired
    private FakeUserController fakeUserController;



//    @DisplayName("FakeUserController createFakeUsers")
//    @BeforeEach
//    void setUp() {
//        fakeUserController.createDummy();
//    }
//
//    @DisplayName("FakeUserController deleteFakeUsers")
//    @AfterEach
//    void tearDown() {
//        fakeUserController.deleteFakeUsers();
//    }

    @Test
    void getFakeUser() {
        ResponseEntity<FakeUserDto> fakeUser_1 = fakeUserController.readFakeUser("fakeuser1");
        FakeUserDto fakeUser_1_Dto = fakeUser_1.getBody();
        ResponseEntity<FakeUserDto> fakeUser_2 = fakeUserController.readFakeUser("fakeuser2");
        FakeUserDto fakeUser_2_Dto = fakeUser_2.getBody();
        ResponseEntity<FakeUserDto> fakeUser_3 = fakeUserController.readFakeUser("fakeuser3");
        FakeUserDto fakeUser_3_Dto = fakeUser_3.getBody();
        ResponseEntity<FakeUserDto> fakeUser_4 = fakeUserController.readFakeUser("fakeuser4");
        FakeUserDto fakeUser_4_Dto = fakeUser_4.getBody();
        ResponseEntity<FakeUserDto> fakeUser_5 = fakeUserController.readFakeUser("fakeuser5");
        FakeUserDto fakeUser_5_Dto = fakeUser_5.getBody();


        assertAll(
                () -> assertThat(fakeUser_1_Dto.getUsername()).isEqualTo("fakeuser1"),
                () -> assertThat(fakeUser_1_Dto.getEmail()).isEqualTo("jeonghwanlee11@gmail.com"),
                () -> assertThat(fakeUser_1_Dto.getAccessToken()).isNotNull(),
                () -> assertThat(fakeUser_1_Dto.getRefreshToken()).isNotNull(),
                () -> assertThat(fakeUser_2_Dto.getUsername()).isEqualTo("fakeuser2"),
                () -> assertThat(fakeUser_2_Dto.getEmail()).isEqualTo("jeonghwanlee12@gmail.com"),
                () -> assertThat(fakeUser_2_Dto.getAccessToken()).isNotNull(),
                () -> assertThat(fakeUser_2_Dto.getRefreshToken()).isNotNull(),
                () -> assertThat(fakeUser_3_Dto.getUsername()).isEqualTo("fakeuser3"),
                () -> assertThat(fakeUser_3_Dto.getEmail()).isEqualTo("jeonghwanlee13@gmail.com"),
                () -> assertThat(fakeUser_3_Dto.getAccessToken()).isNotNull(),
                () -> assertThat(fakeUser_3_Dto.getRefreshToken()).isNotNull(),
                () -> assertThat(fakeUser_4_Dto.getUsername()).isEqualTo("fakeuser4"),
                () -> assertThat(fakeUser_4_Dto.getEmail()).isEqualTo("jeonghwanlee14@gmail.com"),
                () -> assertThat(fakeUser_4_Dto.getAccessToken()).isNotNull(),
                () -> assertThat(fakeUser_4_Dto.getRefreshToken()).isNotNull(),
                () -> assertThat(fakeUser_5_Dto.getUsername()).isEqualTo("fakeuser5"),
                () -> assertThat(fakeUser_5_Dto.getEmail()).isEqualTo("jeonghwanlee15@gmail.com"),
                () -> assertThat(fakeUser_5_Dto.getAccessToken()).isNotNull(),
                () -> assertThat(fakeUser_5_Dto.getRefreshToken()).isNotNull()

        );

    }

}