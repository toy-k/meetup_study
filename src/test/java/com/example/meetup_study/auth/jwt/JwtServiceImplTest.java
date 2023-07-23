//package com.example.meetup_study.auth.jwt;
//
//import com.example.meetup_study.user.UserController;
//import com.example.meetup_study.user.domain.dto.UserDto;
//import com.example.meetup_study.user.fakeUser.FakeUserController;
//import com.example.meetup_study.user.fakeUser.FakeUserDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@DisplayName("JwtServiceImpl")
//class JwtServiceImplTest {
//
//    @Autowired
//    private FakeUserController fakeUserController;
//
//    @Autowired
//    private UserController userController;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private JwtService jwtService;
//
//    @Value("${jwt.access.token.expired}")
//    static private String expiredToken;
//
//    @DisplayName("accessToken expired")
//    @Test
//    void accessTokenExpired() throws Exception {
//        String accessToken = "Bearer "+ expiredToken;
//
//        MvcResult mvcResult = mockMvc.perform(get("/api/user/me")
//                        .header("Authorization", accessToken))
//                .andExpect(status().isUnauthorized())
//                .andReturn();
//
//    }
//
//    @DisplayName("refreshToken expired")
//    @Test
//    void refreshTokenExpired() throws Exception {
//        //given
//        String refreshToken = "Bearer "+ expiredToken;
//
//        MvcResult mvcResult = mockMvc.perform(get("/api/user/me")
//                        .header("Authorization-refresh", refreshToken))
//                .andExpect(status().isUnauthorized())
//                .andReturn();
//        //then
//
//    }
//
//    @DisplayName("accessToken generate by refreshToken")
//    @Test
//    void accessTokenGenerateByRefreshToken() throws Exception {
//
//        ResponseEntity<FakeUserDto> fakeUser_1 = fakeUserController.readFakeUser("fakeuser1");
//        FakeUserDto fakeUser_1_Dto = fakeUser_1.getBody();
//
//        String refreshToken = "Bearer "+ fakeUser_1_Dto.getAccessToken();
//
//        UserDto userDto = new UserDto(fakeUser_1_Dto.getId(), fakeUser_1_Dto.getUsername(), fakeUser_1_Dto.getProfile(), fakeUser_1_Dto.getEmail(), fakeUser_1_Dto.getDescription(),  fakeUser_1_Dto.getRoleType());
//
//        //when
//        MvcResult mvcResult = mockMvc.perform(get("/api/user/me")
//                        .header("Authorization-refresh", refreshToken))
//                .andExpect(status().isOk())
//                .andReturn();
//
//
//        //then
//        UserDto actual = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), UserDto.class);
//
//        assertAll(
//                () -> assertThat(actual.getUsername()).isEqualTo(userDto.getUsername()),
//                () -> assertThat(actual.getEmail()).isEqualTo(userDto.getEmail())
//        );
//    }
//}