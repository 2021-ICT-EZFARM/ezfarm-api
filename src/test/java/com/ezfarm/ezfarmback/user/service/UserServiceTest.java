package com.ezfarm.ezfarmback.user.service;


import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("유저 단위 테스트(Service)")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
        user = User.builder()
                .name("남상우")
                .email("a@gmail.com")
                .password("비밀번호")
                .role(Role.ROLE_USER)
                .build();
    }

    @DisplayName("회원가입을 한다.")
    @Test
    void registerUser() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("남상우", "a@gmail.com", "비밀번호");

        //when
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("비밀번호");
        when(userRepository.save(any())).thenReturn(user);

        //then
        Long userId = userService.registerUser(signUpRequest);
        assertThat(userId).isEqualTo(user.getId());
    }

    @DisplayName("회원가입을 할 때 동일한 이메일이 존재하면 예외 처리를 해준다.")
    @Test
    void registerUserWithException() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("남상우", "a@gmail.com", "비밀번호");


        //when
        when(userRepository.existsByEmail(any())).thenReturn(true);

        //then
        assertThatThrownBy(() -> userService.registerUser(signUpRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하는 이메일입니다.");
    }
}
