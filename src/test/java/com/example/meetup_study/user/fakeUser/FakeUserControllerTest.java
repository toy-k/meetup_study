package com.example.meetup_study.user.fakeUser;

import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.user.domain.ProviderType;
import com.example.meetup_study.user.domain.RoleType;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FakeUserControllerTest {

    @Mock
    private FakeUserServiceImpl fakeUserService;
    @Mock
    private FakeRepository fakeUserRepository;
    @Mock
    private JwtService jwtService;

    private MockMvc mockMvc;

    @InjectMocks
    private FakeUserController fakeUserController;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        //인젝션된 객체
        fakeUserController = new FakeUserController(fakeUserService, fakeUserRepository, jwtService);

        mockMvc = MockMvcBuilders.standaloneSetup(fakeUserController).build();
    }
    @Test
    public void createFakeUser() throws Exception {

        mockMvc.perform(post("/fakeuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("createFakeUser"));
    }

    @Test
    public void deleteFakeUsers() throws Exception {
        //user 6명 생성

        User user1 = null, user2=null, user3=null, user4=null, user5=null;

        for(int i = 1; i<6; i++){
            System.out.println("=-=-=-=-=-=-=-=-=i = " + i);
            User user = new User("fakeusers"+i, null, "fakeuser"+i+"@fake.com", "fakeuser"+i+"description", RoleType.USER, ProviderType.GITHUB, "provider_id");
            if(i==1) user1 = user;
            else if(i==2) user2 = user;
            else if(i==3) user3 = user;
            else if(i==4) user4 = user;
            else if(i==5) user5 = user;
            else throw new Exception("user 생성 실패");
        }


        when(fakeUserRepository.findByUsername("fakeusers1")).thenReturn(Optional.of(user1));
        when(fakeUserRepository.findByUsername("fakeusers2")).thenReturn(Optional.of(user2));
        when(fakeUserRepository.findByUsername("fakeusers3")).thenReturn(Optional.of(user3));
        when(fakeUserRepository.findByUsername("fakeusers4")).thenReturn(Optional.of(user4));
        when(fakeUserRepository.findByUsername("fakeusers5")).thenReturn(Optional.of(user5));


        mockMvc.perform(delete("/fakeuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("deleteFakeUsers"));


        verify(fakeUserRepository, times(1)).findByUsername("fakeusers1");
        verify(fakeUserRepository, times(1)).findByUsername("fakeusers2");
        verify(fakeUserRepository, times(1)).findByUsername("fakeusers3");
        verify(fakeUserRepository, times(1)).findByUsername("fakeusers4");
        verify(fakeUserRepository, times(1)).findByUsername("fakeusers5");

        verify(fakeUserService, times(1)).deleteFakeUser(user1);
        verify(fakeUserService, times(1)).deleteFakeUser(user2);
        verify(fakeUserService, times(1)).deleteFakeUser(user3);
        verify(fakeUserService, times(1)).deleteFakeUser(user4);
        verify(fakeUserService, times(1)).deleteFakeUser(user5);

    }
    @Test
    public void readFakeUser() throws Exception {
        // Mock data
        User user = new User("fakeusers1", null, "fakeuser1@fake.com", "fakeuser1description", RoleType.USER, ProviderType.GITHUB, "provider_id");
        String accessToken = "mocked_access_token";
        String refreshToken = "mocked_refresh_token";


        // DB 역할
        when(fakeUserRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        when(jwtService.generateAccessToken(anyString(), anyLong())).thenReturn(accessToken);
        when(jwtService.generateRefreshToken(anyString(), anyLong())).thenReturn(refreshToken);

        System.out.println("---------" + jwtService.generateAccessToken("asdf@adsf", 1L) );


        mockMvc.perform(get("/fakeuser/fakeusers1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.description").value(user.getDescription()))
                .andDo(print());
//                .andExpect(jsonPath("$.accessToken").value(accessToken))
//                .andExpect(jsonPath("$.refreshToken").value(refreshToken));

        verify(fakeUserRepository, times(1)).findByUsername("fakeusers1");
        verify(jwtService, times(1)).generateAccessToken(user.getEmail(), user.getId());
        verify(jwtService, times(1)).generateRefreshToken(user.getEmail(), user.getId());

    }

    @Test
    public void readFakeUser_UserNotFoundException(){
        String username = "testuser";

        when(fakeUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> fakeUserController.readFakeUser(username));
    }

    @Test
    public void findAllTest(){

        List<User> userList = new ArrayList<>();

        for (int i = 1; i<6; i++){
            userList.add(new User("fakeusers"+i, null, "fakeuser"+i+"@fake.com", "fakeuser"+i+"description", RoleType.USER, ProviderType.GITHUB, "provider_id"));
        }

        when(fakeUserService.findAll()).thenReturn(userList);

        ResponseEntity<List<User>> responseEntity = fakeUserController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userList, responseEntity.getBody());
    }
}