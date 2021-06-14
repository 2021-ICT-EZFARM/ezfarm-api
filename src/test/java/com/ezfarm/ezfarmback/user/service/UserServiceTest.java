package com.ezfarm.ezfarmback.user.service;


import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
    void createUser() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("남상우", "a@gmail.com", "비밀번호");
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("비밀번호");
        when(userRepository.save(any())).thenReturn(user);

        //when
        userService.createUser(signUpRequest);

        //then
        verify(userRepository).existsByEmail(any());
        verify(passwordEncoder).encode(any());
        verify(userRepository).save(any());
    }

    @DisplayName("회원가입을 할 때 동일한 이메일이 존재하면 예외 처리를 해준다.")
    @Test
    void createUserWithException() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("남상우", "a@gmail.com", "비밀번호");
        when(userRepository.existsByEmail(any())).thenReturn(true);

        //when, then
        assertThatThrownBy(() -> userService.createUser(signUpRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DUPLICATED_EMAIL.getMessage());
    }

    @DisplayName("유저 정보를 수정한다.")
    @Test
    void updateUser() {
        //given
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
                "user",
                "010-1234-1234",
                "address",
                "image");

        //when
        user.updateUser(userUpdateRequest);

        //then
        Assertions.assertAll(
                () -> assertThat(user.getName()).isEqualTo(userUpdateRequest.getName()),
                () -> assertThat(user.getPhoneNumber()).isEqualTo(userUpdateRequest.getPhoneNumber()),
                () -> assertThat(user.getAddress()).isEqualTo(userUpdateRequest.getAddress()),
                () -> assertThat(user.getImageUrl()).isEqualTo(userUpdateRequest.getImageUrl())
        );
    }

    @DisplayName("유저 정보를 수정 시 해당하는 유저가 없으면 예외 처리한다.")
    @Test
    void updateUserWithException() {
        //given
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
                "user",
                "010-1234-1234",
                "address",
                "image");

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userService.updateUser(user, userUpdateRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NON_EXISTENT_USER.getMessage());
    }
}
