package com.ezfarm.ezfarmback.user.controller;

import com.ezfarm.ezfarmback.common.controller.CommonApiTest;
import com.ezfarm.ezfarmback.common.WithMockCustomUser;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest;
import com.ezfarm.ezfarmback.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static java.util.Optional.ofNullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저 단위 테스트(Controller)")
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest extends CommonApiTest{

    @MockBean
    UserService userService;

    @DisplayName("유저 회원가입을 한다.")
    @Test
    void createUser() throws Exception {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("highright", "highright@mail.com", "비밀번호");

        //when, then
        when(userService.createUser(any())).thenReturn(1L);

        mockMvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @DisplayName("유저 정보를 조회한다.")
    @WithMockCustomUser
    @Test
    void readUser() throws Exception {
        User user = User.builder()
                .name("남상우")
                .email("a@gmail.com")
                .password("비밀번호")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmail(any())).thenReturn(ofNullable(user));

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("유저 정보를 수정한다.")
    @WithMockCustomUser
    @Test
    void updateUser() throws Exception {
        //given
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
                "user",
                "010-1234-1234",
                "address",
                "image");

        //when, then
        doNothing().when(userService).updateUser(any(), any());

        mockMvc.perform(patch("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("회원 탈퇴한다.")
    @WithMockCustomUser
    @Test
    void deleteUser() throws Exception {
        //given
        doNothing().when(userRepository).deleteAllById(any());

        //when, then
        mockMvc.perform(delete("/api/user"))
                .andExpect(status().isNoContent());
    }
}

