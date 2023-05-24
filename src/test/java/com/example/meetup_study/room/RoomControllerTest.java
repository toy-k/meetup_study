package com.example.meetup_study.room;

import com.example.meetup_study.room.domain.Category;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.user.fakeUser.FakeUserController;
import com.example.meetup_study.user.fakeUser.FakeUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("RoomController")
class RoomControllerTest {

    @Autowired
    private FakeUserController fakeUserController;

    @Autowired
    private RoomController roomController;

    @Autowired
    private MockMvc mockMvc;

    private RoomDto createdRoomDto;
    private RequestRoomDto requestRoomDto;
    private String accessToken;

    private List<Long> roomIds = new ArrayList<>();

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @DisplayName("FakeUserController createFakeUsers")
    @BeforeEach
    void setUp() throws Exception {
        this.createRoomObject();
        this.createRoomList();
    }


    @DisplayName("createRoom")
    @Test
    void createRoom() {

        assertAll(
                () -> assertNotNull(createdRoomDto.getId()),
                () -> assertEquals(requestRoomDto.getTitle(), createdRoomDto.getTitle()),
                () -> assertEquals(requestRoomDto.getDescription(), createdRoomDto.getDescription()),
                () -> assertEquals(requestRoomDto.getJoinEndDate(), createdRoomDto.getJoinEndDate()),
                () -> assertEquals(requestRoomDto.getJoinEndDate(), createdRoomDto.getJoinEndDate()),
                () -> assertEquals(requestRoomDto.getMeetupStartDate(), createdRoomDto.getMeetupStartDate()),
                () -> assertEquals(requestRoomDto.getMeetupLocation(), createdRoomDto.getMeetupLocation()),
                () -> assertEquals(requestRoomDto.getMeetupPhotoUrl(), createdRoomDto.getMeetupPhotoUrl()),
                () -> assertEquals(requestRoomDto.getCategory(), createdRoomDto.getCategory()),
                () -> assertEquals(requestRoomDto.getHostUserId(), createdRoomDto.getHostUser().getId())
        );
    }

    @Test
    void getRoom() {
    }

    @Test
    void getRoomList() {
    }

    @Test
    void getRoomListBeforeMeetupStart() {
    }

    @Test
    void getRoomListAfterMeetupStart() {
    }

    @Test
    void updateRoom() {
    }

    @Test
    void deleteRoom() {
    }


    private void createRoomObject()throws Exception{
        System.out.println("1===============================");
        ResponseEntity<FakeUserDto> fakeUser = fakeUserController.readFakeUser("fakeuser1");
        FakeUserDto fakeUserDto = fakeUser.getBody();
        String accessToken = "Bearer "+ fakeUserDto.getAccessToken();

        // create a new room
        RequestRoomDto requestRoomDto = new RequestRoomDto(
                "Room title",
                "Room description",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now().plusDays(14),
                "Location",
                "https://example.com/photo.jpg",
                Category.CAFE,
                fakeUserDto.getId(),
                1
        );

        System.out.println("2===============================");

        String requestBody = objectMapper.writeValueAsString(requestRoomDto);

        System.out.println("3===============================");

        MvcResult mvcResult = mockMvc.perform(post("/api/room")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("33===============================");


        String responseBody = mvcResult.getResponse().getContentAsString();
        RoomDto createdRoomDto = objectMapper.readValue(responseBody, RoomDto.class);

        System.out.println("4===============================");

        this.requestRoomDto = requestRoomDto;
        this.createdRoomDto = createdRoomDto;
        this.accessToken = accessToken;
    }

    private void createRoomList() throws Exception{
        System.out.println("5===============================");
        ResponseEntity<FakeUserDto> fakeUser_1 = fakeUserController.readFakeUser("fakeuser1");
        FakeUserDto fakeUser_1_Dto = fakeUser_1.getBody();

        String accessToken = "Bearer "+ fakeUser_1_Dto.getAccessToken();
        List<Long> roomIds = new ArrayList<>();

        System.out.println("6===============================");

        for (int i = 1; i < 6; i++) {

            RequestRoomDto requestRoomDto;
            if(i%2 == 0){
                requestRoomDto = new RequestRoomDto(
                        "Room " + i,
                        "Description " + i,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(7),
                        LocalDateTime.now().plusDays(14),
                        "Location " + i,
                        "https://example.com/photo" + i + ".jpg",
                        Category.CAFE,
                        1L,
                        1
                );
            }else{
                requestRoomDto = new RequestRoomDto(
                        "Room " + i,
                        "Description " + i,
                        LocalDateTime.now(),
                        LocalDateTime.now().minusDays(7),
                        LocalDateTime.now().minusDays(14),
                        "Location " + i,
                        "https://example.com/photo" + i + ".jpg",
                        Category.CAFE,
                        1L,
                        1
                );
            }

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
            RoomDto createdRoomDto = objectMapper.readValue(responseBody, RoomDto.class);
            System.out.println("9===============================");
            roomIds.add(createdRoomDto.getId());
        }
        this.roomIds = roomIds;
    }
}