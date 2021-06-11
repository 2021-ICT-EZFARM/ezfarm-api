package com.ezfarm.ezfarmback.user.controller;

import com.ezfarm.ezfarmback.security.local.CustomUserDetailsService;
import com.ezfarm.ezfarmback.security.local.TokenProvider;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CorsFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
@DisplayName("유저 단위 테스트(Controller)")
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    TokenProvider tokenProvider;

    @MockBean
    CorsFilter corsFilter;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    //private Map

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {


        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("유저 회원가입을 한다.")
    @WithMockUser(roles = "USER")
    @Test
    void registerUser() throws Exception {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("highright", "highright@mail.com", "비밀번호");

        //when, then
        when(userService.registerUser(any())).thenReturn(1L);

        mockMvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated());
    }
}

