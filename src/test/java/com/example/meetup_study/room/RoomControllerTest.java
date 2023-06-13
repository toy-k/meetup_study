package com.example.meetup_study.room;


import com.example.meetup_study.Category.CategoryService;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class RoomControllerTest {

    @Mock
    private RoomService roomService;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserService userService;
    @Mock
    private CategoryService categoryService;

    private RoomController roomController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        roomController = new RoomController(roomService, jwtService, userService, categoryService);

        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    void createRoomTest() throws Exception {

    }
}