package com.example.meetup_study.review;

import com.example.meetup_study.Category.CategoryService;
import com.example.meetup_study.Category.domain.CategoryEnum;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.review.domain.dto.RequestDeleteReviewDto;
import com.example.meetup_study.review.domain.dto.RequestReviewDto;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import com.example.meetup_study.room.RoomService;
import com.example.meetup_study.room.domain.RoomStatus;
import com.example.meetup_study.room.domain.RoomType;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.fakeUser.FakeRepository;
import com.example.meetup_study.user.fakeUser.FakeUserController;
import com.example.meetup_study.user.fakeUser.FakeUserDto;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

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
    private FakeUserDto fakeUserDto;
    private ReviewDto createdReviewDto;
    private RequestReviewDto requestReviewDto;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    public void setup() throws Exception {
        //initController.createDummy()

        this.createRoomObject();
        this.createReviewObject();


    }


    @Nested
    @DisplayName("createReviewTest")
    class CreateReviewTestSuite {
        @Test
        void createReview() throws Exception{

            assertAll(
                    () -> assertEquals(requestReviewDto.getRoomId(), createdReviewDto.getRoomId()),
                    () -> assertEquals(requestReviewDto.getRating(), createdReviewDto.getRating()),
                    () -> assertEquals(requestReviewDto.getContent(), createdReviewDto.getContent())
            );
        }

        @Nested
        @DisplayName("createReview exception")
        class CreateReviewExceptionTestSuite {
            @Test
            void createReview_AccessTokenInvalidRequestException() throws Exception{
                String accessToken = "fakeAccessToken";


                String requestBody = objectMapper.writeValueAsString(requestReviewDto);


                try{
                    mockMvc.perform(post("/api/review")
                                    .header("Authorization", accessToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                            .andDo(print())
                            .andExpect(status().isUnauthorized());
                }catch(Exception e){
                    fail("Expected exception was not thrown");
                }
            }
            @Test
            void createReview_UserNotFoundException() throws Exception{
                String accessToken = "Bearer " + jwtService.generateAccessToken("fake@email.com", 444L);
                String requestBody = objectMapper.writeValueAsString(requestReviewDto);


                try{
                    mockMvc.perform(post("/api/review")
                                    .header("Authorization", accessToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                            .andDo(print())
                            .andExpect(status().isNotFound());
                }catch(Exception e){
                    fail("Expected exception was not thrown");
                }
            }
            @Test
            void createReview_RoomNotFoundException() throws Exception{
                RequestReviewDto newRequestReviewDto = requestReviewDto;
                newRequestReviewDto.setRoomId(444L);

                String requestBody = objectMapper.writeValueAsString(newRequestReviewDto);


                try{
                    mockMvc.perform(post("/api/review")
                                    .header("Authorization", accessToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                            .andDo(print())
                            .andExpect(status().isNotFound());
                }catch(Exception e){
                    fail("Expected exception was not thrown");
                }
            }
            //    @Test
//    void createReview_ReviewInvalidRequestException_yet() throws Exception{
//        //진행중인 모임에 리뷰를 당장 달지 못하는 테스트
//
//    }
//    @Test
//    void createReview_JoinedUserNotFoundException() throws Exception{
//
//        //실제 방에 참여하지 않은 유저가 댓글 못남기는 테스트
//    }
            @Test
            void createReview_ReviewInvalidRequestException_exist() throws Exception{

                String requestBody = objectMapper.writeValueAsString(requestReviewDto);

                try{
                    mockMvc.perform(post("/api/review")
                                    .header("Authorization", accessToken)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                            .andDo(print())
                            .andExpect(status().isBadRequest());
                }catch(Exception e){
                    fail("Expected exception was not thrown");
                }

            }
        }

    }



    @Nested
    @DisplayName("getReviewTest")
    class GetReviewTestSuite {

        @Test
        void getReview() throws Exception{
            int size = 1;
            Long roomId = createdRoomDto.getId();

            MvcResult mvcResult = mockMvc.perform(get("/api/review/roomId/{roomId}", roomId)
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(size))

                    .andExpect(status().isOk())
                    .andReturn();
        }

        @Nested
        @DisplayName("getReview exception")
        class GetReviewExceptionTestSuite {
            @Test
            void getReview_RoomNotFoundException() throws Exception {
                Long roomId = 444L;

                try{
                    mockMvc.perform(get("/api/review/roomId/{roomId}", roomId)
                            )
                            .andDo(print())
                            .andExpect(status().isNotFound());

                }catch(Exception e){
                    fail("Expected exception was not thrown");
                }

            }

        }
    }





    @Nested
    @DisplayName("getReviewTest")
    class GetReviewByUserIdTestSuite {
        @Test
        void getReviewByUserId() throws Exception{
            Long userId = fakeUserDto.getId();

            MvcResult mvcResult = mockMvc.perform(get("/api/review/userId/{userId}", userId))
                    .andDo(print())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(status().isOk())
                    .andReturn();
        }

        @Nested
        @DisplayName("getReview exception")
        class GetReviewByUserIdExceptionTestSuite {
            @Test
            void getReviewByUserId_UserNotFoundException() throws Exception {
                Long userId = 444L;

                try{
                    mockMvc.perform(get("/api/review/userId/{userId}", userId))
                            .andDo(print())
                            .andExpect(status().isNotFound());

                }catch(Exception e){
                    fail("Expected exception was not thrown");
                }

            }

        }
    }


    @Nested
    @DisplayName("deleteReviewTest")
    class DeleteReviewTestSuite {
        @Test
        void deleteReview() throws Exception{
            RequestDeleteReviewDto requestDeleteReviewDto = new RequestDeleteReviewDto(
                    createdReviewDto.getId()
            );


            MvcResult mvcResult = mockMvc.perform(delete("/api/review")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDeleteReviewDto))
                            .header("Authorization", accessToken))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String responseBody = mvcResult.getResponse().getContentAsString();
            ReviewDto deletedReviewDto = objectMapper.readValue(responseBody, ReviewDto.class);

            assertAll(
                    () -> assertEquals(requestDeleteReviewDto.getReviewId(), deletedReviewDto.getId())

            );
        }
        @Nested
        @DisplayName("deleteReview exception")
        class DeleteReviewExceptionTestSuite {

            @Test
            void deleteReview_AccessTokenInvalidRequestException() throws Exception {
                String accessToken = "fakeAccessToken";

                RequestDeleteReviewDto requestDeleteReviewDto = new RequestDeleteReviewDto(
                        createdReviewDto.getId()
                );

                try{
                    mockMvc.perform(delete("/api/review")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(requestDeleteReviewDto))
                                    .header("Authorization",accessToken))
                            .andDo(print())
                            .andExpect(status().isUnauthorized());

                }catch(Exception e){
                    fail("Expected exception was not thrown");
                }

            }
            @Test
            void deleteReview_UserNotFoundException() throws Exception {

                String accessToken = "Bearer " + jwtService.generateAccessToken("fake@email.com", 444L);

                RequestDeleteReviewDto requestDeleteReviewDto = new RequestDeleteReviewDto(
                        createdReviewDto.getId()
                );

                try{
                    mockMvc.perform(delete("/api/review")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(requestDeleteReviewDto))
                                    .header("Authorization",accessToken))
                            .andDo(print())
                            .andExpect(status().isNotFound());

                }catch(Exception e){
                    fail("Expected exception was not thrown");
                }

            }
            @Test
            void deleteReview_ReviewNotFoundException() throws Exception {
                RequestDeleteReviewDto requestDeleteReviewDto = new RequestDeleteReviewDto(444L);

                try{
                    mockMvc.perform(delete("/api/review")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(requestDeleteReviewDto))
                                    .header("Authorization",accessToken))
                            .andDo(print())
                            .andExpect(status().isNotFound());

                }catch(Exception e){
                    fail("Expected exception was not thrown");
                }

            }
        //    @Test
        //    void deleteReview_RoomNotFoundException() throws Exception {
        //        //리뷰가 속한 룸이 없는 경우 테스트
        //    }
        }
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
        this.fakeUserDto = fakeUserDto;
    }

    private void createReviewObject() throws Exception{
        RequestReviewDto requestReviewDto = new RequestReviewDto(
                this.createdRoomDto.getId(),
                3,
                "reviewContent"
        );

        String requestBody = objectMapper.writeValueAsString(requestReviewDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/review")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        ReviewDto reviewDto = objectMapper.readValue(responseBody, ReviewDto.class);

        this.createdReviewDto = reviewDto;
        this.requestReviewDto = requestReviewDto;
    }
}