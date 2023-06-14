package com.example.meetup_study.room;


import com.example.meetup_study.Category.CategoryService;
import com.example.meetup_study.Category.domain.Category;
import com.example.meetup_study.Category.domain.CategoryEnum;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.image.roomImage.domain.RoomImage;
import com.example.meetup_study.room.domain.RoomStatus;
import com.example.meetup_study.room.domain.RoomType;
import com.example.meetup_study.room.domain.dto.RequestDeleteRoomDto;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.fakeUser.FakeRepository;
import com.example.meetup_study.user.fakeUser.FakeUserController;
import com.example.meetup_study.user.fakeUser.FakeUserDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest {

    @Autowired
    private RoomService roomService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private FakeRepository fakeUserRepository;
    @Autowired
    private FakeUserController fakeUserController;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;


    private RoomDto createdRoomDto;
    private RequestRoomDto requestRoomDto;
    private String accessToken;

    private RoomDto updateRoomDto;

    private List<Long> roomIds = new ArrayList<>();

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    public void setup() throws Exception {
        //initController.createDummy()


        this.createRoomObject();
        this.createRoomList();


    }
    @Nested
    @DisplayName("createRoomTest")
    class CreateRoomTestSuite {


        @Test
        void createRoomTest() throws Exception {

            assertAll(
                    () -> assertNotNull(createdRoomDto.getId()),
                    () -> assertEquals(requestRoomDto.getTitle(), createdRoomDto.getTitle()),
                    () -> assertEquals(requestRoomDto.getDescription(), createdRoomDto.getDescription()),
                    () -> assertEquals(requestRoomDto.getMeetupStartDate(), createdRoomDto.getMeetupStartDate()),
                    () -> assertEquals(requestRoomDto.getMeetupEndDate(), createdRoomDto.getMeetupEndDate()),
                    () -> assertEquals(requestRoomDto.getLocation(), createdRoomDto.getLocation()),
                    () -> assertEquals(requestRoomDto.getCategory(), createdRoomDto.getCategory()),
                    () -> assertEquals(requestRoomDto.getHostUserId(), createdRoomDto.getHostUserId())
            );
        }

        @Nested
        @DisplayName("createRoom exception")
        class CreateRoomExceptionTestSuite {

            @Test
            void createRoomTest_AccessTokenInvalidRequestException() throws Exception {
                String accessToken = "fakeAccessToken";

                String requestBody = objectMapper.writeValueAsString(requestRoomDto);

                try {
                    mockMvc.perform(post("/api/room")
                                    .header("Authorization", accessToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                            .andDo(print())
                            .andExpect(status().isUnauthorized());
                } catch (NestedServletException e) {
                    fail("Expected exception was not thrown");
                }

            }

            @Test
            void createRoomTest_UserNotFoundException() throws Exception {
                String accessToken = "Bearer " + jwtService.generateAccessToken("fake@email.com", 444L);
                String requestBody = objectMapper.writeValueAsString(requestRoomDto);

                try {
                    mockMvc.perform(post("/api/room")
                                    .header("Authorization", accessToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                            .andDo(print())
                            .andExpect(status().isUnauthorized());
                    //                    .andExpect(status().isNotFound());
                } catch (NestedServletException e) {
                    fail("Expected exception was not thrown");
                }
            }


            @Test
            void createRoomTest_RoomInvalidRequestException_user() throws Exception {
                RequestRoomDto exceptionRequestRoomDto = requestRoomDto;
                exceptionRequestRoomDto.setHostUserId(444L);
                String requestBody = objectMapper.writeValueAsString(exceptionRequestRoomDto);

                try {
                    mockMvc.perform(post("/api/room")
                                    .header("Authorization", accessToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                            .andDo(print())
                            .andExpect(status().isBadRequest());
                } catch (NestedServletException e) {
                    fail("Expected exception was not thrown");
                }
            }

            @Test
            void createRoomTest_RoomInvalidRequestException_date() throws Exception {
                RequestRoomDto exceptionRequestRoomDto = requestRoomDto;

                exceptionRequestRoomDto.setMeetupStartDate(LocalDateTime.now().minusDays(2));
                exceptionRequestRoomDto.setMeetupEndDate(LocalDateTime.now().minusDays(4));
                String requestBody = objectMapper.writeValueAsString(exceptionRequestRoomDto);

                try {
                    mockMvc.perform(post("/api/room")
                                    .header("Authorization", accessToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                            .andDo(print())
                            .andExpect(status().isBadRequest());
                } catch (NestedServletException e) {
                    fail("Expected exception was not thrown");
                }
            }


            @Test
            void createRoomTest_RoomInvalidRequestException_price() throws Exception {
                RequestRoomDto exceptionRequestRoomDto = requestRoomDto;
                exceptionRequestRoomDto.setPrice(-1L);
                String requestBody = objectMapper.writeValueAsString(exceptionRequestRoomDto);

                try {
                    mockMvc.perform(post("/api/room")
                                    .header("Authorization", accessToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                            .andDo(print())
                            .andExpect(status().isBadRequest());
                } catch (NestedServletException e) {
                    fail("Expected exception was not thrown");
                }
            }
        }
    }

    @Test
    void getRoomTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/api/room/id/13")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        RoomDto roomDto = objectMapper.readValue(responseBody, RoomDto.class);



        assertAll(
                () -> assertNotNull(roomDto.getId()),
                () -> assertNotNull(roomDto.getTitle()),
                () -> assertNotNull(roomDto.getDescription()),
                () -> assertNotNull(roomDto.getMeetupStartDate()),
                () -> assertNotNull(roomDto.getMeetupEndDate()),
                () -> assertNotNull(roomDto.getLocation()),
                () -> assertNotNull(roomDto.getCategory()),
                () -> assertNotNull(roomDto.getRoomStatus()),
                () -> assertNotNull(roomDto.getRoomType()),
                () -> assertNotNull(roomDto.getPrice()),
                () -> assertNotNull(roomDto.getCurrentJoinNumber()),
                () -> assertNotNull(roomDto.getMaxJoinNumber()),
                () -> assertNotNull(roomDto.getHostUserId())
        );

    }

    @Test
    void getRoomListTest() throws Exception {
        int page = 1;
        int size = 10;

        mockMvc.perform(get("/api/room/list")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(size))
                .andDo(print());
    }

    @Test
    void getRoomListBeforeMeetupStartTest() throws Exception {
        int page = 1;
        int size = 10;

        MvcResult mvcResult = mockMvc.perform(get("/api/room/list/before-meetup-start")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andDo(print())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        List<RoomDto> roomDtoList = objectMapper.readValue(responseBody, new TypeReference<>() {});

        LocalDateTime now = LocalDateTime.now();

        for (RoomDto roomDto : roomDtoList) {
            if(!now.isBefore(roomDto.getMeetupStartDate())){
                fail("Expected exception was not thrown");
            }

        }
    }


    @Test
    void getRoomListAfterMeetupStartTest() throws Exception {
        int page = 1;
        int size = 10;

        MvcResult mvcResult = mockMvc.perform(get("/api/room/list/after-meetup-start")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andDo(print())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        List<RoomDto> roomDtoList = objectMapper.readValue(responseBody, new TypeReference<>() {});

        LocalDateTime now = LocalDateTime.now();

        for (RoomDto roomDto : roomDtoList) {
            if(now.isBefore(roomDto.getMeetupStartDate())){
                fail("Expected exception was not thrown");
            }

        }

    }


    @Nested
    @DisplayName("updateRoomTest")
    class UpdateRoomTestSuite {
        @Test
        void updateRoomTest() throws Exception {

            String requestBody = objectMapper.writeValueAsString(updateRoomDto);

            MvcResult mvcResult = mockMvc.perform(put("/api/room")
                            .header("Authorization", accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();

            String responseBody = mvcResult.getResponse().getContentAsString();
            RoomDto updatedRoomDto = objectMapper.readValue(responseBody, RoomDto.class);

            assertAll(
                    () -> assertEquals(updateRoomDto.getId(), updatedRoomDto.getId()),
                    () -> assertEquals(updateRoomDto.getTitle(), updatedRoomDto.getTitle()),
                    () -> assertEquals(updateRoomDto.getDescription(), updatedRoomDto.getDescription()),
                    () -> assertEquals(updateRoomDto.getMeetupStartDate(), updatedRoomDto.getMeetupStartDate()),
                    () -> assertEquals(updateRoomDto.getMeetupEndDate(), updatedRoomDto.getMeetupEndDate()),
                    () -> assertEquals(updateRoomDto.getLocation(), updatedRoomDto.getLocation()),
                    () -> assertEquals(updateRoomDto.getCategory(), updatedRoomDto.getCategory()),
                    () -> assertEquals(updateRoomDto.getRoomStatus(), updatedRoomDto.getRoomStatus()),
                    () -> assertEquals(updateRoomDto.getRoomType(), updatedRoomDto.getRoomType()),
                    () -> assertEquals(updateRoomDto.getPrice(), updatedRoomDto.getPrice()),
                    () -> assertEquals(updateRoomDto.getCurrentJoinNumber(), updatedRoomDto.getCurrentJoinNumber()),
                    () -> assertEquals(updateRoomDto.getMaxJoinNumber(), updatedRoomDto.getMaxJoinNumber()),
                    () -> assertEquals(updateRoomDto.getHostUserId(), updatedRoomDto.getHostUserId())

            );


        }

        @Nested
        @DisplayName("updateRoom exception")
        class UpdateRoomExceptionTestSuite {
            @Test
            void updateRoomTest_AccessTokenInvalidRequestException() throws Exception {
                String accessToken = "fakeAccessToken";

                String requestBody = objectMapper.writeValueAsString(updateRoomDto);

                try {
                    mockMvc.perform(put("/api/room")
                                    .header("Authorization", accessToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                            .andDo(print())
                            .andExpect(status().isUnauthorized());
                } catch (NestedServletException e) {
                    fail("Expected exception was not thrown");
                }

            }
            @Test
            void updateRoomTest_UserNotFoundException() throws Exception {
                String accessToken = "Bearer " + jwtService.generateAccessToken("fake@email.com", 444L);
                String requestBody = objectMapper.writeValueAsString(updateRoomDto);

                try {
                    mockMvc.perform(put("/api/room")
                                    .header("Authorization", accessToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                            .andDo(print())
                            .andExpect(status().isUnauthorized());
                    //                    .andExpect(status().isNotFound());
                } catch (NestedServletException e) {
                    fail("Expected exception was not thrown");
                }

            }
            @Test
            void updateRoomTest_RoomInvalidRequestException_user() throws Exception {
                RoomDto exceptionRoomDto = updateRoomDto;
                exceptionRoomDto.setHostUserId(444L);
                String requestBody = objectMapper.writeValueAsString(updateRoomDto);

                try {
                    mockMvc.perform(put("/api/room")
                                    .header("Authorization", accessToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                            .andDo(print())
                            .andExpect(status().isBadRequest());
                    //                    .andExpect(status().isNotFound());
                } catch (NestedServletException e) {
                    fail("Expected exception was not thrown");
                }

            }
            @Test
            void updateRoomTest_RoomInvalidRequestException_date() throws Exception {
                RoomDto exceptionRoomDto = updateRoomDto;

                exceptionRoomDto.setMeetupStartDate(LocalDateTime.now().minusDays(2));
                exceptionRoomDto.setMeetupEndDate(LocalDateTime.now().minusDays(4));
                String requestBody = objectMapper.writeValueAsString(exceptionRoomDto);

                try {
                    mockMvc.perform(put("/api/room")
                                    .header("Authorization", accessToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                            .andDo(print())
                            .andExpect(status().isBadRequest());
                } catch (NestedServletException e) {
                    fail("Expected exception was not thrown");
                }

            }
            //        @Test
//        void updateRoomTest_RoomInvalidRequestException_category() throws Exception {
//            RequestRoomDto exceptionRequestRoomDto = requestRoomDto;
//            exceptionRequestRoomDto.setCategory("ASDF");
//            String requestBody = objectMapper.writeValueAsString(exceptionRequestRoomDto);
//
//            try {
//                mockMvc.perform(post("/api/room")
//                                .header("Authorization", accessToken)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(requestBody))
//                        .andDo(print())
//                        .andExpect(status().isBadRequest());
//            } catch (NestedServletException e) {
//                fail("Expected exception was not thrown");
//            }
//
//        }
            @Test
            void updateRoomTest_RoomInvalidRequestException_price() throws Exception {
                RoomDto exceptionRoomDto = updateRoomDto;
                exceptionRoomDto.setPrice(-1L);
                String requestBody = objectMapper.writeValueAsString(exceptionRoomDto);

                try {
                    mockMvc.perform(put("/api/room")
                                    .header("Authorization", accessToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                            .andDo(print())
                            .andExpect(status().isBadRequest());
                } catch (NestedServletException e) {
                    fail("Expected exception was not thrown");
                }

            }
        }



    }

        @Test
        void deleteRoomTest() throws Exception {

            RequestDeleteRoomDto requestDeleteRoomDto = new RequestDeleteRoomDto(createdRoomDto.getId());

            String requestDeleteBody = objectMapper.writeValueAsString(requestDeleteRoomDto);
            // when
            MvcResult deletedMvcResult = mockMvc.perform(delete("/api/room")
                            .header("Authorization",accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestDeleteBody))
                    .andExpect(status().isOk())
                    .andReturn();

            String deletedResponseBody = deletedMvcResult.getResponse().getContentAsString();
            RoomDto deletedRoomDtoResponse = objectMapper.readValue(deletedResponseBody, RoomDto.class);

            // then
            assertThat(deletedRoomDtoResponse.getId()).isEqualTo(createdRoomDto.getId());
        }


















    private void createRoomObject()throws Exception{

        ResponseEntity<FakeUserDto> fakeUser = fakeUserController.readFakeUser("fakeuser1");

        FakeUserDto fakeUserDto = fakeUser.getBody();

        String accessToken = "Bearer "+ fakeUserDto.getAccessToken();

        // create a new room
        RequestRoomDto requestRoomDto = new RequestRoomDto(
        "createRoomTest",
    "createRoomTest_desc",
                CategoryEnum.ALCHOLE,
        "meetupLocation",
                LocalDateTime.now().minusDays(4),
                 LocalDateTime.now().minusDays(3),
10,
1,
        10000L,
                RoomStatus.OPEN,
                RoomType.ONLINE,
    1L,
                fakeUserDto.getId(),
    "meetupPhotoPath"

        );


        String requestBody = objectMapper.writeValueAsString(requestRoomDto);


        MvcResult mvcResult = mockMvc.perform(post("/api/room")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();



        String responseBody = mvcResult.getResponse().getContentAsString();
        RoomDto createdRoomDto = objectMapper.readValue(responseBody, RoomDto.class);


        RoomDto roomDto = new RoomDto(
                13L,
                "update_title",
                "update_desc",
                CategoryEnum.ALCHOLE,
                "meetupLocation",
                LocalDateTime.now().minusDays(4),
                LocalDateTime.now().minusDays(3),
                9,
                1,
                30000L,
                RoomStatus.OPEN,
                RoomType.ONLINE,
                1L,
                fakeUserDto.getId()
        );


        this.requestRoomDto = requestRoomDto;
        this.createdRoomDto = createdRoomDto;
        this.accessToken = accessToken;
        this.updateRoomDto = roomDto;
    }

    private void createRoomList() throws Exception{
        System.out.println("5===============================");
        ResponseEntity<FakeUserDto> fakeUser_1 = fakeUserController.readFakeUser("fakeuser1");
        FakeUserDto fakeUser_1_Dto = fakeUser_1.getBody();

        String accessToken = "Bearer "+ fakeUser_1_Dto.getAccessToken();
        List<Long> roomIds = new ArrayList<>();

        System.out.println("6===============================");
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
        Integer maxJoinNumber;
        Long price;
        RoomStatus roomStatus;
        RoomType roomType;

        for (int i = 1; i < 5; i++) {

            RequestRoomDto requestRoomDto;

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
            hostUserId = fakeUser_1_Dto.getId();
            currentJoinNumber = 1;
            viewCount =1L;
            maxJoinNumber = 10;
            price = 10000L;
            roomStatus = RoomStatus.OPEN;
            roomType = RoomType.ONLINE;

            requestRoomDto = new RequestRoomDto(title, desc, category, meetupLocation, meetupStartDate, meetupEndDate, maxJoinNumber, currentJoinNumber, price, roomStatus, roomType, viewCount, hostUserId, meetupPhotoPath);

            System.out.println("7===============================");
            String requestBody = objectMapper.writeValueAsString(requestRoomDto);

            MvcResult mvcResult = mockMvc.perform(post("/api/room")
                            .header("Authorization", accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andReturn();

            System.out.println("8===============================");
            String responseBody = mvcResult.getResponse().getContentAsString();
            RoomDto newRoomDto = objectMapper.readValue(responseBody, RoomDto.class);
            System.out.println("9===============================");
            roomIds.add(newRoomDto.getId());
        }
        this.roomIds = roomIds;
    }
}