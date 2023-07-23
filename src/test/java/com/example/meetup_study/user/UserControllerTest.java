//package com.example.meetup_study.user;
//
//import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
//import com.example.meetup_study.auth.jwt.JwtService;
//import com.example.meetup_study.user.domain.RoleType;
//import com.example.meetup_study.user.domain.dto.RequestUserDto;
//import com.example.meetup_study.user.domain.dto.UserDto;
//import com.example.meetup_study.user.domain.repository.UserRepository;
//import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.util.NestedServletException;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//class UserControllerTest {
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private JwtService jwtService;
//
//    private MockMvc mockMvc;
//
//    @InjectMocks
//    private UserController userController;
//
//
//    @Value("${jwt.access.token.expired}")
//    static private String expiredToken;
//
//
//    @BeforeEach
//    public void setup(){
//        MockitoAnnotations.openMocks(this);
//
//        userController = new UserController(userService, jwtService);
//
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
//
//    @Test
//    void findUserByIdTest() throws Exception {
//
//        UserDto userDto = new UserDto(1L, "fakeuser1", null, "fakeuser1@fake.com", "desc", RoleType.USER);
//
//
//        when(userService.findByIdReturnDto(1L)).thenReturn(Optional.of(userDto));
//
//        mockMvc.perform(get("/api/user/id/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.username").value("fakeuser1"));
//
//
////        verify(userService, times(1)).findByIdReturnDto(2L);
//
//    }
//
//    @Test
//    void findUserByIdTest_UserNotFoundException(){
//
//        Long userId = 1L;
//
//        when(userService.findByIdReturnDto(userId)).thenThrow(new UserNotFoundException());
//
//        assertThrows(UserNotFoundException.class, () -> {
//            userService.findByIdReturnDto(userId);
//        });
//
//        verify(userService, times(1)).findByIdReturnDto(1L);
//    }
//
//    @Test
//    void findMeByToken() throws Exception {
//
//        UserDto userDto= new UserDto(1L, "fakeuser1", null, "fakeUser1@fake.com", "desc",RoleType.USER);
//        String accessToken = "fakeAccessToken";
//
//        MockHttpServletRequest req = new MockHttpServletRequest();
//        req.setAttribute("AccessToken", accessToken);
//
//        when(jwtService.extractUserId(accessToken)).thenReturn(Optional.of(1L));
//        when(userService.findByIdReturnDto(1L)).thenReturn(Optional.of(userDto));
//
//        mockMvc.perform(get("/api/user/me")
//                .requestAttr("AccessToken", accessToken))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.username").value("fakeuser1"));
//
//        verify(jwtService, times(1)).extractUserId(accessToken);
//        verify(userService, times(1)).findByIdReturnDto(1L);
//    }
//
//    @Test
//    void findMeByToken_UserNotFoundException() throws Exception{
//        UserDto userDto= new UserDto(1L, "fakeuser1", null, "fakeUser1@fake.com", "desc",RoleType.USER);
//        String accessToken = "fakeAccessToken";
//
//        MockHttpServletRequest req = new MockHttpServletRequest();
//        req.setAttribute("AccessToken", accessToken);
//
//        when(jwtService.extractUserId(accessToken)).thenReturn(Optional.of(2L));
//        when(userService.findByIdReturnDto(1L)).thenReturn(Optional.of(userDto));
//
//        try {
//            mockMvc.perform(get("/api/user/me")
//                    .requestAttr("AccessToken", accessToken));
//        } catch (NestedServletException e) {
//            Throwable cause = e.getCause();
//            assertTrue(cause instanceof UserNotFoundException);
////            System.out.println("-----------------------------------");
////            System.out.println(e);
////            System.out.println(cause);
////            System.out.println(cause.getClass());
////            System.out.println(cause instanceof UserNotFoundException);
////            System.out.println("-----------------------------------");
////
//        }
//
//        verify(jwtService, times(1)).extractUserId(accessToken);
//        verify(userService, times(1)).findByIdReturnDto(2L);
//
//    }
//
//    @Test
//    void findMeByToken_AccessTokenInvalidRequestException() throws Exception{
//
//        UserDto userDto= new UserDto(1L, "fakeuser1", null, "fakeUser1@fake.com", "desc",RoleType.USER);
//        String accessToken = "fakeAccessToken";
//
//        MockHttpServletRequest req = new MockHttpServletRequest();
//        req.setAttribute("AccessToken", accessToken);
//
//        when(jwtService.extractUserId(accessToken)).thenReturn(Optional.empty());
//
//        try {
//            mockMvc.perform(get("/api/user/me")
//                    .requestAttr("AccessToken", accessToken));
//        } catch (NestedServletException e) {
//            Throwable cause = e.getCause();
//            assertTrue(cause instanceof AccessTokenInvalidRequestException);
//        }
//
//        verify(jwtService, times(1)).extractUserId(accessToken);
//    }
//
//    @Test
//    void updateMeTest() throws Exception {
//
//        String accessToken = "fakeAccessToken";
//
//        UserDto updatedUserDto = new UserDto(1L, "updatedUsername", null, "updatedEmail", "updatedDescription",RoleType.USER);
//
//        when(jwtService.extractUserId(accessToken)).thenReturn(Optional.of(1L));
//        when(userService.updateUser(anyLong(), any())).thenReturn(Optional.of(updatedUserDto));
//
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        MockHttpServletRequest req = new MockHttpServletRequest();
//        req.setAttribute("AccessToken", accessToken);
//
//        RequestUserDto requestUserDto = new RequestUserDto("updatedUsername", "updatedEmail", "updatedDescription");
//
//
//        mockMvc.perform(put("/api/user/me")
//                .requestAttr("AccessToken", accessToken)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(requestUserDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.username").value(updatedUserDto.getUsername()))
//                .andExpect(jsonPath("$.email").value(updatedUserDto.getEmail()))
//                .andExpect(jsonPath("$.description").value(updatedUserDto.getDescription()));
//
//
//        verify(jwtService, times(1)).extractUserId(accessToken);
//        verify(userService, times(1)).updateUser(anyLong(), any());
//
//
//    }
//
//
//    @Test
//    void updateMeTest_UserNotFoundExceptioin() throws Exception{
//        String accessToken = "fakeAccessToken";
//
//        UserDto updatedUserDto = new UserDto(1L, "updatedUsername", null, "updatedEmail", "updatedDescription",RoleType.USER);
//
//        when(jwtService.extractUserId(accessToken)).thenReturn(Optional.of(1L));
//        when(userService.updateUser(anyLong(), any())).thenReturn(Optional.empty());
//
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        MockHttpServletRequest req = new MockHttpServletRequest();
//        req.setAttribute("AccessToken", accessToken);
//
//        RequestUserDto requestUserDto = new RequestUserDto("updatedUsername", "updatedEmail", "updatedDescription");
//
//
//        try{
//            mockMvc.perform(put("/api/user/me")
//                            .requestAttr("AccessToken", accessToken)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(requestUserDto)));
//
//        }catch(NestedServletException e){
//            Throwable cause = e.getCause();
//            assertTrue(cause instanceof UserNotFoundException);
//        }
//
//
//        verify(jwtService, times(1)).extractUserId(accessToken);
//        verify(userService, times(1)).updateUser(anyLong(), any());
//
//    }
//
//    @Test
//    void updateMeTest_AccessTokenInvalidRequestException() throws Exception{
//        String accessToken = "fakeAccessToken";
//
//        UserDto updatedUserDto = new UserDto(1L, "updatedUsername", null, "updatedEmail", "updatedDescription",RoleType.USER);
//
//        when(jwtService.extractUserId(accessToken)).thenReturn(Optional.empty());
//
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        MockHttpServletRequest req = new MockHttpServletRequest();
//        req.setAttribute("AccessToken", accessToken);
//
//        RequestUserDto requestUserDto = new RequestUserDto("updatedUsername", "updatedEmail", "updatedDescription");
//
//
//        try{
//            mockMvc.perform(put("/api/user/me")
//                    .requestAttr("AccessToken", accessToken)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(requestUserDto)));
//
//        }catch(NestedServletException e){
//            Throwable cause = e.getCause();
//            assertTrue(cause instanceof AccessTokenInvalidRequestException);
//        }
//
//
//        verify(jwtService, times(1)).extractUserId(accessToken);
//
//    }
//
//}