//package com.example.meetup_study.joinedUser;
//
//import com.example.meetup_study.Category.CategoryService;
//import com.example.meetup_study.Category.domain.CategoryEnum;
//import com.example.meetup_study.auth.jwt.JwtService;
//import com.example.meetup_study.joinedUser.domain.dto.JoinedUserDto;
//import com.example.meetup_study.joinedUser.domain.dto.RequestJoinedUserDto;
//import com.example.meetup_study.review.domain.dto.RequestReviewDto;
//import com.example.meetup_study.review.domain.dto.ReviewDto;
//import com.example.meetup_study.room.service.RoomService;
//import com.example.meetup_study.room.domain.enums.RoomStatus;
//import com.example.meetup_study.room.domain.enums.RoomType;
//import com.example.meetup_study.room.domain.dto.RequestRoomDto;
//import com.example.meetup_study.room.domain.dto.RoomDto;
//import com.example.meetup_study.user.UserService;
//import com.example.meetup_study.user.fakeUser.FakeRepository;
//import com.example.meetup_study.user.fakeUser.FakeUserController;
//import com.example.meetup_study.user.fakeUser.FakeUserDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class JoinedUserControllerTest {
//
//    @Autowired
//    private RoomService roomService;
//    @Autowired
//    private JwtService jwtService;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private FakeRepository fakeUserRepository;
//    @Autowired
//    private FakeUserController fakeUserController;
//    @Autowired
//    private CategoryService categoryService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//
//    private RoomDto createdRoomDto;
//    private RequestRoomDto requestRoomDto;
//    private String accessToken;
//
//    private FakeUserDto fakeUserDto;
//
//    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
//
//    @BeforeEach
//    public void setup() throws Exception {
//        //initController.createDummy()
//
//        this.createRoomObject();
//    }
//
//    //현재코드 중복 요소 많음 예외는 나중처리
//
//    @Test
//    void joinRoom() throws Exception{
//        ResponseEntity<FakeUserDto> fakeUser2 = fakeUserController.readFakeUser("fakeuser2");
//
//        FakeUserDto fakeUserDto2 = fakeUser2.getBody();
//        String accessToken = "Bearer "+ fakeUserDto2.getAccessToken();
//
//        RequestJoinedUserDto requestJoinedUserDto = new RequestJoinedUserDto(
//                createdRoomDto.getId(),
//                fakeUserDto2.getId()
//        );
//
//        String requestBody = objectMapper.writeValueAsString(requestJoinedUserDto);
//
//        MvcResult mvcResult = mockMvc.perform(post("/api/joinedUser")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String responseBody = mvcResult.getResponse().getContentAsString();
//        JoinedUserDto joinedUserDto = objectMapper.readValue(responseBody, JoinedUserDto.class);
//
//        assertAll(
//                ()->assertEquals(createdRoomDto.getId(), joinedUserDto.getRoomId()),
//                ()->assertEquals(fakeUserDto2.getId(), joinedUserDto.getUserId())
//        );
//
//    }
//
//    @Test
//    void leaveRoom() throws Exception{
//        ResponseEntity<FakeUserDto> fakeUser3 = fakeUserController.readFakeUser("fakeuser3");
//
//        FakeUserDto fakeUserDto3 = fakeUser3.getBody();
//        String accessToken = "Bearer "+ fakeUserDto3.getAccessToken();
//
//        RequestJoinedUserDto requestJoinedUserDto = new RequestJoinedUserDto(
//                createdRoomDto.getId(),
//                fakeUserDto3.getId()
//        );
//
//        String requestBody = objectMapper.writeValueAsString(requestJoinedUserDto);
//
//        mockMvc.perform(post("/api/joinedUser")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk())
//                .andReturn();
//
//
//        MvcResult mvcResult = mockMvc.perform(delete("/api/joinedUser")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String responseBody = mvcResult.getResponse().getContentAsString();
//        JoinedUserDto leaveUserDto = objectMapper.readValue(responseBody, JoinedUserDto.class);
//
//        assertAll(
//                ()->assertEquals(createdRoomDto.getId(), leaveUserDto.getRoomId()),
//                ()->assertEquals(fakeUserDto3.getId(), leaveUserDto.getUserId())
//        );
//
//    }
//
//    @Test
//    void getJoinedUser() throws Exception{
//        ResponseEntity<FakeUserDto> fakeUser4 = fakeUserController.readFakeUser("fakeuser4");
//
//        FakeUserDto fakeUserDto4 = fakeUser4.getBody();
//        String accessToken = "Bearer "+ fakeUserDto4.getAccessToken();
//
//        RequestJoinedUserDto requestJoinedUserDto = new RequestJoinedUserDto(
//                createdRoomDto.getId(),
//                fakeUserDto4.getId()
//        );
//
//        String requestBody = objectMapper.writeValueAsString(requestJoinedUserDto);
//
//        MvcResult mvcResult = mockMvc.perform(post("/api/joinedUser")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String responseBody = mvcResult.getResponse().getContentAsString();
//        JoinedUserDto joinedUserDto = objectMapper.readValue(responseBody, JoinedUserDto.class);
//
//        Long joinedUserId = joinedUserDto.getId();
//
//        MvcResult mvcResult2 = mockMvc.perform(get("/api/joinedUser/id/{id}",joinedUserId))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String responseBody2 = mvcResult2.getResponse().getContentAsString();
//
//        JoinedUserDto joinedUserDto2 = objectMapper.readValue(responseBody2, JoinedUserDto.class);
//
//        assertAll(
//                ()->assertEquals(createdRoomDto.getId(), joinedUserDto2.getRoomId()),
//                ()->assertEquals(fakeUserDto4.getId(), joinedUserDto2.getUserId())
//        );
//
//    }
//
//    @Test
//    void testGetJoinedUser() throws Exception{
//        ResponseEntity<FakeUserDto> fakeUser5 = fakeUserController.readFakeUser("fakeuser5");
//
//        FakeUserDto fakeUserDto5 = fakeUser5.getBody();
//        String accessToken = "Bearer "+ fakeUserDto5.getAccessToken();
//
//        RequestJoinedUserDto requestJoinedUserDto = new RequestJoinedUserDto(
//                createdRoomDto.getId(),
//                fakeUserDto5.getId()
//        );
//
//        String requestBody = objectMapper.writeValueAsString(requestJoinedUserDto);
//
//        MvcResult mvcResult = mockMvc.perform(post("/api/joinedUser")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String responseBody = mvcResult.getResponse().getContentAsString();
//        JoinedUserDto joinedUserDto = objectMapper.readValue(responseBody, JoinedUserDto.class);
//
//        Long joinedUserId = joinedUserDto.getId();
//
//        MvcResult mvcResult2 = mockMvc.perform(get("/api/joinedUser/ids/roomId/{roomId}/userId/{userId}", createdRoomDto.getId(), fakeUserDto5.getId()))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String responseBody2 = mvcResult2.getResponse().getContentAsString();
//
//        JoinedUserDto joinedUserDto2 = objectMapper.readValue(responseBody2, JoinedUserDto.class);
//
//        assertAll(
//                ()->assertEquals(createdRoomDto.getId(), joinedUserDto2.getRoomId()),
//                ()->assertEquals(fakeUserDto5.getId(), joinedUserDto2.getUserId())
//        );
//
//    }
//
//    @Test
//    void getJoinedUserByUserId() throws Exception{
//        //추가조건 필요
//        mockMvc.perform(get("/api/joinedUser/userId/{userId}", fakeUserDto.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andDo(print());
//    }
//
//    @Test
//    void getJoinedUserByRoomId() throws Exception{
//        //추가조건필요
//        mockMvc.perform(get("/api/joinedUser/roomId/{roomId}", createdRoomDto.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andDo(print());
//
//    }
//
//
//
//
//    private void createRoomObject()throws Exception{
//
//        ResponseEntity<FakeUserDto> fakeUser = fakeUserController.readFakeUser("fakeuser1");
//
//        FakeUserDto fakeUserDto = fakeUser.getBody();
//
//        String accessToken = "Bearer "+ fakeUserDto.getAccessToken();
//
//        // create a new room
//        RequestRoomDto requestRoomDto = new RequestRoomDto(
//                "createRoomTest",
//                "createRoomTest_desc",
//                CategoryEnum.ALCHOLE,
//                "meetupLocation",
//                LocalDateTime.now().minusDays(4),
//                LocalDateTime.now().minusDays(3),
//                10,
//                1,
//                10000L,
//                RoomStatus.OPEN,
//                RoomType.ONLINE,
//                1L,
//                fakeUserDto.getId(),
//                "meetupPhotoPath"
//
//        );
//
//
//        String requestBody = objectMapper.writeValueAsString(requestRoomDto);
//
//
//        MvcResult mvcResult = mockMvc.perform(post("/api/room")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk())
//                .andReturn();
//
//
//
//        String responseBody = mvcResult.getResponse().getContentAsString();
//        RoomDto createdRoomDto = objectMapper.readValue(responseBody, RoomDto.class);
//
//
//        RoomDto roomDto = new RoomDto(
//                13L,
//                "update_title",
//                "update_desc",
//                CategoryEnum.ALCHOLE,
//                "meetupLocation",
//                LocalDateTime.now().minusDays(4),
//                LocalDateTime.now().minusDays(3),
//                9,
//                1,
//                30000L,
//                RoomStatus.OPEN,
//                RoomType.ONLINE,
//                1L,
//                fakeUserDto.getId(),
//                LocalDateTime.now(),
//                LocalDateTime.now()
//
//        );
//
//
//        this.requestRoomDto = requestRoomDto;
//        this.createdRoomDto = createdRoomDto;
//        this.accessToken = accessToken;
//        this.fakeUserDto = fakeUserDto;
//    }
//}