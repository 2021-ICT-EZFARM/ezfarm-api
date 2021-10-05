package com.ezfarm.ezfarmback.user.service;


import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.common.utils.fileupload.FileStoreUtils;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest.IsDefaultImage;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("유저 단위 테스트(Service)")
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private FileStoreUtils fileStoreUtils;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private ModelMapper modelMapper;

  private UserService userService;

  private User user;

  @BeforeEach
  void setUp() {
    userService = new UserService(userRepository, fileStoreUtils, passwordEncoder, modelMapper);
    user = User.builder()
        .name("남상우")
        .email("a@gmail.com")
        .password("비밀번호")
        .imageUrl("image")
        .role(Role.ROLE_USER)
        .build();
  }

  @DisplayName("회원가입을 한다.")
  @Test
  void createUser() {
    SignUpRequest signUpRequest = new SignUpRequest("남상우", "a@gmail.com", "비밀번호");
    when(userRepository.existsByEmail(any())).thenReturn(false);
    when(passwordEncoder.encode(any())).thenReturn("비밀번호");
    when(userRepository.save(any())).thenReturn(user);

    userService.createUser(signUpRequest);

    verify(userRepository).existsByEmail(any());
    verify(passwordEncoder).encode(any());
    verify(userRepository).save(any());
  }

  @DisplayName("회원가입을 할 때 동일한 이메일이 존재하면 예외 처리를 해준다.")
  @Test
  void createUserWithException() {
    SignUpRequest signUpRequest = new SignUpRequest("남상우", "a@gmail.com", "비밀번호");
    when(userRepository.existsByEmail(any())).thenReturn(true);

    assertThatThrownBy(() -> userService.createUser(signUpRequest))
        .isInstanceOf(CustomException.class)
        .hasMessage(ErrorCode.DUPLICATED_EMAIL.getMessage());
  }

  @DisplayName("유저 정보를 수정한다. 프로필은 변경하지 않는다.")
  @Test
  void updateUser() {
    UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
        .phoneNumber("010-0000-0000")
        .address("수정된 주소")
        .isDefaultImage(IsDefaultImage.N)
        .build();

    when(userRepository.findById(any())).thenReturn(ofNullable(user));
    userService.updateUser(user, userUpdateRequest);

    Assertions.assertAll(
        () -> assertThat(user.getPhoneNumber()).isEqualTo(userUpdateRequest.getPhoneNumber()),
        () -> assertThat(user.getAddress()).isEqualTo(userUpdateRequest.getAddress()),
        () -> assertThat(user.getImageUrl()).isEqualTo(user.getImageUrl())
    );
  }

  @DisplayName("유저 정보를 수정 시 해당하는 유저가 없으면 예외 처리한다.")
  @Test
  void updateUserWithException() {
    UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().build();

    when(userRepository.findById(any())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.updateUser(user, userUpdateRequest))
        .isInstanceOf(CustomException.class)
        .hasMessage(ErrorCode.NON_EXISTENT_USER.getMessage());
  }

  @DisplayName("유저 정보를 수정 시 프로필을 기본 이미지로 변경한다.")
  @Test
  void updateUserProfile_defaultImage() {
    UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
        .isDefaultImage(IsDefaultImage.Y)
        .build();

    when(userRepository.findById(any())).thenReturn(ofNullable(user));
    doNothing().when(fileStoreUtils).deleteFile(any());

    userService.updateUser(user, userUpdateRequest);

    assertThat(user.getImageUrl()).isNull();
  }

  @DisplayName("유저 정보를 수정 시 프로필을 새로운 이미지로 변경한다.")
  @Test
  void updateUserProfile_newImage() {
    String content = "content";
    UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
        .image(new MockMultipartFile("test", content.getBytes(StandardCharsets.UTF_8)))
        .isDefaultImage(IsDefaultImage.N)
        .build();

    when(userRepository.findById(any())).thenReturn(ofNullable(user));
    doNothing().when(fileStoreUtils).deleteFile(any());
    when(fileStoreUtils.storeFile(any())).thenReturn("new-image");

    userService.updateUser(user, userUpdateRequest);

    Assertions.assertAll(
        () -> assertThat(user.getImageUrl()).isEqualTo("new-image")
    );
  }
}
