package com.example.meetup_study.user;

import com.example.meetup_study.user.domain.dto.UserDto;
import com.example.meetup_study.user.fakeUser.FakeUserController;
import com.example.meetup_study.user.fakeUser.FakeUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UserController")
class UserControllerTest {

    @Autowired
    private FakeUserController fakeUserController;

    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Value("${jwt.access.token.expired}")
    static private String expiredToken;


    @DisplayName("findUserById")
    @Test
    void findUserById() {
        ResponseEntity<FakeUserDto> fakeUser_1 = fakeUserController.readFakeUser("fakeuser1");
        Long fakeUser_1_DtoId = fakeUser_1.getBody().getId();

        ResponseEntity<UserDto> userDtoRes = userController.findUserById(fakeUser_1_DtoId);

        UserDto userDto = userDtoRes.getBody();
        assertAll(
                () -> assertThat(userDto.getUsername()).isEqualTo("fakeuser1"),
                () -> assertThat(userDto.getEmail()).isEqualTo("jeonghwanlee11@gmail.com")
        );

    }

    @DisplayName("findUserByIdWithNotExistUser with exception")
    @Test
    void findUserByIdWithNotExistUser() {
        assertThrows(IllegalArgumentException.class, () -> {
            userController.findUserById(-1L);
        });

    }

    @DisplayName("findMeByToken with accesstoken")
    @Test
    void findMeByToken() throws Exception{
        //given
        ResponseEntity<FakeUserDto> fakeUser_1 = fakeUserController.readFakeUser("fakeuser1");
        FakeUserDto fakeUser_1_Dto = fakeUser_1.getBody();

        String accessToken = "Bearer "+ fakeUser_1_Dto.getAccessToken();

        System.out.println("accessToken = " + accessToken);

        UserDto userDto = new UserDto(fakeUser_1_Dto.getId(), fakeUser_1_Dto.getUsername(), fakeUser_1_Dto.getProfile(), fakeUser_1_Dto.getEmail(), fakeUser_1_Dto.getDescription());

        System.out.println("userDto = " + userDto.getEmail());

        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/user/me")
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andReturn();


        System.out.println("mvcResult.getResponse().getContentAsString() = " + mvcResult.getResponse());

        //then
        UserDto actual = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), UserDto.class);

        assertAll(
                () -> assertThat(actual.getUsername()).isEqualTo(userDto.getUsername()),
                () -> assertThat(actual.getEmail()).isEqualTo(userDto.getEmail())
        );

    }

    @DisplayName("findMe with expired accesstoken")
    @Test
    void findMeWithExpiredToken() throws Exception {


        String accessToken = "Bearer "+ expiredToken;

        MvcResult mvcResult = mockMvc.perform(get("/api/user/me")
                        .header("Authorization", accessToken))
                .andExpect(status().isUnauthorized())
                .andReturn();

    }

    @DisplayName("UpdateMe with accesstoken and body")
    @Test
    void updateMe() throws Exception {
        //given
        ResponseEntity<FakeUserDto> fakeUser_1 = fakeUserController.readFakeUser("fakeuser1");
        FakeUserDto fakeUser_1_Dto = fakeUser_1.getBody();
        String accessToken = "Bearer "+ fakeUser_1_Dto.getAccessToken();
        UserDto userDto = new UserDto(fakeUser_1_Dto.getId(), fakeUser_1_Dto.getUsername(), fakeUser_1_Dto.getProfile(), fakeUser_1_Dto.getEmail(), "updatedDescription");

        //when
        MvcResult mvcResult = mockMvc.perform(put("/api/user/me")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn();

        //then
        UserDto actual = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), UserDto.class);

        assertAll(
                () -> assertThat(actual.getUsername()).isEqualTo(userDto.getUsername()),
                () -> assertThat(actual.getEmail()).isEqualTo(userDto.getEmail()),
                () -> assertThat(actual.getProfile()).isEqualTo("updatedImageUrl"),
                () -> assertThat(actual.getDescription()).isEqualTo("updatedDescription")
        );
    }


    @DisplayName("UpdateMe with expired accesstoken")
    @Test
    void updateMeWithExpiredToken() throws Exception {
        //given
        ResponseEntity<FakeUserDto> fakeUser_1 = fakeUserController.readFakeUser("fakeuser1");
        FakeUserDto fakeUser_1_Dto = fakeUser_1.getBody();

        String accessToken = "Bearer " + expiredToken;
        UserDto userDto = new UserDto(fakeUser_1_Dto.getId(), fakeUser_1_Dto.getUsername(), fakeUser_1_Dto.getProfile(), fakeUser_1_Dto.getEmail(), "updatedDescription");

        //when
        MvcResult mvcResult = mockMvc.perform(put("/api/user/me")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @DisplayName("UpdateMe with not exist user")
    @Test
    void updateMewithNotExistUser() throws Exception{
        //given
        ResponseEntity<FakeUserDto> fakeUser_1 = fakeUserController.readFakeUser("fakeuser1");
        FakeUserDto fakeUser_1_Dto = fakeUser_1.getBody();

        String accessToken = "Bearer "+ fakeUser_1_Dto.getAccessToken();
        UserDto userDto = new UserDto(-1L, fakeUser_1_Dto.getUsername(), fakeUser_1_Dto.getProfile(), fakeUser_1_Dto.getEmail(), "updatedDescription");

        //when
        MvcResult mvcResult = mockMvc.perform(put("/user/me")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}