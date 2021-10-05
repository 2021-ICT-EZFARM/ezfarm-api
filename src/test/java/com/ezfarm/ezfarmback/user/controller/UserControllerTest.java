package com.ezfarm.ezfarmback.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezfarm.ezfarmback.common.WithMockCustomUser;
import com.ezfarm.ezfarmback.common.controller.CommonApiTest;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.dto.UserResponse;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest;
import com.ezfarm.ezfarmback.user.dto.UserUpdateResponse;
import com.ezfarm.ezfarmback.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@DisplayName("유저 단위 테스트(Controller)")
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest extends CommonApiTest {

  @MockBean
  UserService userService;

  @DisplayName("유저 회원가입을 한다.")
  @Test
  void createUser() throws Exception {
    SignUpRequest signUpRequest = new SignUpRequest("highright", "highright@mail.com", "비밀번호");

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
  void findUser() throws Exception {
    UserResponse response = UserResponse.builder()
        .id(1L)
        .name("남상우")
        .address("서울특별시")
        .phoneNumber("010-1234-1234")
        .imageUrl("이미지")
        .email("이메일")
        .build();

    when(userService.findUser(any())).thenReturn(response);

    mockMvc.perform(get("/api/user"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @DisplayName("유저 정보를 수정한다.")
  @WithMockCustomUser
  @Test
  void updateUser() throws Exception {
    UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().build();

    when(userService.updateUser(any(), any())).thenReturn(new UserUpdateResponse());

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
    doNothing().when(userRepository).deleteById(any());

    mockMvc.perform(delete("/api/user"))
        .andExpect(status().isNoContent());
  }
}

