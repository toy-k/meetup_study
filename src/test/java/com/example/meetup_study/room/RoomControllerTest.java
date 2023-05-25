package com.example.meetup_study.room;

import com.example.meetup_study.room.domain.Category;
import com.example.meetup_study.room.domain.dto.RequestDeleteRoomDto;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.user.fakeUser.FakeUserController;
import com.example.meetup_study.user.fakeUser.FakeUserDto;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                () -> assertEquals(requestRoomDto.getHostUserId(), createdRoomDto.getHostUserId())
        );
    }

    @DisplayName("getRoom")
    @Test
    void getRoom() throws Exception{

        Long roomId = createdRoomDto.getId();

        MvcResult mvcResult2 = mockMvc.perform(get("/api/room/id/"+roomId))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody2 = mvcResult2.getResponse().getContentAsString();
        RoomDto createdRoomDto2 = objectMapper.readValue(responseBody2, RoomDto.class);

        assertAll(
                () -> assertNotNull(createdRoomDto.getId()),
                () -> assertEquals(requestRoomDto.getTitle(), createdRoomDto2.getTitle()),
                () -> assertEquals(requestRoomDto.getDescription(), createdRoomDto2.getDescription()),
                () -> assertEquals(requestRoomDto.getJoinEndDate(), createdRoomDto2.getJoinEndDate()),
                () -> assertEquals(requestRoomDto.getJoinEndDate(), createdRoomDto2.getJoinEndDate()),
                () -> assertEquals(requestRoomDto.getMeetupStartDate(), createdRoomDto2.getMeetupStartDate()),
                () -> assertEquals(requestRoomDto.getMeetupLocation(), createdRoomDto2.getMeetupLocation()),
                () -> assertEquals(requestRoomDto.getMeetupPhotoUrl(), createdRoomDto2.getMeetupPhotoUrl()),
                () -> assertEquals(requestRoomDto.getCategory(), createdRoomDto2.getCategory()),
                () -> assertEquals(requestRoomDto.getHostUserId(), createdRoomDto2.getHostUserId())
        );
    }

    @DisplayName("getRoom not exist")
    @Test
    void getRoomNotExist(){
        Long roomId = -1L;

        assertThrows(IllegalArgumentException.class, () ->  {
            roomController.getRoom(roomId);
        });

    }
    @DisplayName("getRoomList")
    @Test
    void getRoomList() throws Exception {

        List<Long> roomIds = new ArrayList<>();
        for(int i = 1; i <= this.roomIds.size(); i++){
            roomIds.add(this.roomIds.get(i-1));
        }


        MvcResult mvcResult2 = mockMvc.perform(get("/api/room/list"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody2 = mvcResult2.getResponse().getContentAsString();
        List<RoomDto> roomDtos = objectMapper.readValue(responseBody2, new TypeReference<List<RoomDto>>() {});


        for (Long roomId : roomIds) {
            boolean found = false;
            for (RoomDto roomDto : roomDtos) {
                if (roomDto.getId().equals(roomId)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Room " + roomId + " not found in the list");
        }
    }

    @DisplayName("getRoomListBeforeMeetupStart = 현재 보다 미팅 시작날짜가 지난 경우")
    @Test
    void getRoomListBeforeMeetupStart() throws Exception {

        List<Long> roomIds = new ArrayList<>();
        for(int i = 1; i <= this.roomIds.size(); i++){
            if(i%2 != 0) roomIds.add(this.roomIds.get(i-1));
        }


        MvcResult mvcResult2 = mockMvc.perform(get("/api/room/list/before-meetup-start"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody2 = mvcResult2.getResponse().getContentAsString();
        List<RoomDto> roomDtos = objectMapper.readValue(responseBody2, new TypeReference<List<RoomDto>>() {});


        for (Long roomId : roomIds) {
            boolean found = false;
            for (RoomDto roomDto : roomDtos) {
                if (roomDto.getId().equals(roomId)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Room " + roomId + " not found in the list");
        }
    }

    @DisplayName("getRoomListAfterMeetupStart = 현재 보다 미팅 시작날짜가 안 지난 경우")
    @Test
    void getRoomListAfterMeetupStart() throws Exception {


        List<Long> roomIds = new ArrayList<>();
        for(int i = 1; i <= this.roomIds.size(); i++){
            if(i%2 == 0) roomIds.add(this.roomIds.get(i-1));
        }


        MvcResult mvcResult2 = mockMvc.perform(get("/api/room/list/after-meetup-start"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody2 = mvcResult2.getResponse().getContentAsString();
        List<RoomDto> roomDtos = objectMapper.readValue(responseBody2, new TypeReference<List<RoomDto>>() {});


        for (Long roomId : roomIds) {
            boolean found = false;
            for (RoomDto roomDto : roomDtos) {
                if (roomDto.getId().equals(roomId)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Room " + roomId + " not found in the list");
        }
    }


    @DisplayName("updateRoom")
    @Test
    void updateRoom() throws Exception {


        String updatedTitle = "Updated room title";
        String updatedDescription = "Updated room description";
        LocalDateTime updatedJoinEndDate = LocalDateTime.now().plusDays(2);
        LocalDateTime updatedMeetupStartDate = LocalDateTime.now().plusDays(9);
        LocalDateTime updatedMeetupEndDate = LocalDateTime.now().plusDays(16);
        String updatedMeetupLocation = "Updated location";
        String updatedMeetupPhotoUrl = "https://example.com/updated_photo.jpg";
        Category updatedCategory = Category.STUDY;

        RoomDto updatedRoomDto = new RoomDto(
                createdRoomDto.getId(),
                updatedTitle,
                updatedDescription,
                updatedJoinEndDate,
                updatedMeetupStartDate,
                updatedMeetupEndDate,
                updatedMeetupLocation,
                updatedMeetupPhotoUrl,
                updatedCategory,
                createdRoomDto.getHostUserId(),
                createdRoomDto.getJoinNumber()
        );

        String updatedRequestBody = objectMapper.writeValueAsString(updatedRoomDto);


        MvcResult updatedMvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/room")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRequestBody))
                .andExpect(status().isOk())
                .andReturn();

        String updatedResponseBody = updatedMvcResult.getResponse().getContentAsString();
        RoomDto updatedRoomDtoResponse = objectMapper.readValue(updatedResponseBody, RoomDto.class);


        assertAll("Room fields should match updated values",
                () -> assertThat(updatedRoomDtoResponse.getTitle()).isEqualTo(updatedTitle),
                () -> assertThat(updatedRoomDtoResponse.getDescription()).isEqualTo(updatedDescription),
                () -> assertThat(updatedRoomDtoResponse.getJoinEndDate()).isEqualTo(updatedJoinEndDate),
                () -> assertThat(updatedRoomDtoResponse.getJoinEndDate()).isEqualTo(updatedJoinEndDate),
                () -> assertThat(updatedRoomDtoResponse.getMeetupStartDate()).isEqualTo(updatedMeetupStartDate),
                () -> assertThat(updatedRoomDtoResponse.getMeetupLocation()).isEqualTo(updatedMeetupLocation),
                () -> assertThat(updatedRoomDtoResponse.getMeetupPhotoUrl()).isEqualTo(updatedMeetupPhotoUrl),
                () -> assertThat(updatedRoomDtoResponse.getCategory()).isEqualTo(updatedCategory)
        );
    }


    @DisplayName("deleteRoom")
    @Test
    void deleteRoom() throws Exception {

        RequestDeleteRoomDto requestDeleteRoomDto = new RequestDeleteRoomDto(createdRoomDto.getId());

        String requestDeleteBody = objectMapper.writeValueAsString(requestDeleteRoomDto);

        MvcResult deletedMvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/room")
                        .header("Authorization",accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestDeleteBody))
                .andExpect(status().isOk())
                .andReturn();

        String deletedResponseBody = deletedMvcResult.getResponse().getContentAsString();
        RoomDto deletedRoomDtoResponse = objectMapper.readValue(deletedResponseBody, RoomDto.class);

        assertThat(deletedRoomDtoResponse.getId()).isEqualTo(createdRoomDto.getId());
        assertThat(deletedRoomDtoResponse.getTitle()).isEqualTo(createdRoomDto.getTitle());
        assertThat(deletedRoomDtoResponse.getDescription()).isEqualTo(createdRoomDto.getDescription());
        assertThat(deletedRoomDtoResponse.getJoinEndDate()).isEqualTo(createdRoomDto.getJoinEndDate());
        assertThat(deletedRoomDtoResponse.getMeetupStartDate()).isEqualTo(createdRoomDto.getMeetupStartDate());
        assertThat(deletedRoomDtoResponse.getMeetupEndDate()).isEqualTo(createdRoomDto.getMeetupEndDate());
        assertThat(deletedRoomDtoResponse.getMeetupLocation()).isEqualTo(createdRoomDto.getMeetupLocation());
        assertThat(deletedRoomDtoResponse.getMeetupPhotoUrl()).isEqualTo(createdRoomDto.getMeetupPhotoUrl());
        assertThat(deletedRoomDtoResponse.getCategory()).isEqualTo(createdRoomDto.getCategory());
    }



    private void createRoomObject()throws Exception{
        ResponseEntity<FakeUserDto> fakeUser = fakeUserController.readFakeUser("fakeuser1");
        FakeUserDto fakeUserDto = fakeUser.getBody();
        String accessToken = "Bearer "+ fakeUserDto.getAccessToken();

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

        String requestBody = objectMapper.writeValueAsString(requestRoomDto);


        MvcResult mvcResult = mockMvc.perform(post("/api/room")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();


        String responseBody = mvcResult.getResponse().getContentAsString();
        RoomDto createdRoomDto = objectMapper.readValue(responseBody, RoomDto.class);


        this.requestRoomDto = requestRoomDto;
        this.createdRoomDto = createdRoomDto;
        this.accessToken = accessToken;
    }

    private void createRoomList() throws Exception{
        ResponseEntity<FakeUserDto> fakeUser_1 = fakeUserController.readFakeUser("fakeuser1");
        FakeUserDto fakeUser_1_Dto = fakeUser_1.getBody();

        String accessToken = "Bearer "+ fakeUser_1_Dto.getAccessToken();
        List<Long> roomIds = new ArrayList<>();

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

            String requestBody = objectMapper.writeValueAsString(requestRoomDto);

            MvcResult mvcResult = mockMvc.perform(post("/api/room")
                            .header("Authorization", accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andReturn();

            String responseBody = mvcResult.getResponse().getContentAsString();
            RoomDto createdRoomDto = objectMapper.readValue(responseBody, RoomDto.class);
            roomIds.add(createdRoomDto.getId());
        }
        this.roomIds = roomIds;
    }
}