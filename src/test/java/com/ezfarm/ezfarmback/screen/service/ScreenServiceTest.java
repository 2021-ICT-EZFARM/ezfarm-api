package com.ezfarm.ezfarmback.screen.service;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.common.utils.iot.IotUtils;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.screen.domain.Screen;
import com.ezfarm.ezfarmback.screen.dto.ScreenResponse;
import com.ezfarm.ezfarmback.screen.domain.ScreenRepository;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@DisplayName("실시간 화면 단위 테스트")
@ExtendWith(MockitoExtension.class)
public class ScreenServiceTest {

  ScreenService screenService;

  @Mock
  IotUtils iotUtils;

  @Mock
  ScreenRepository screenRepository;

  @Mock
  FarmRepository farmRepository;

  @Mock
  ModelMapper modelMapper;

  Farm farm;

  User user;

  Screen screen;

  ScreenResponse screenResponse;

  @BeforeEach()
  void setUp() {
    screenService = new ScreenService(iotUtils, screenRepository, farmRepository, modelMapper);

    user = User.builder()
        .id(1L)
        .name("테스트 이름")
        .email("test@email.com")
        .password("비밀번호")
        .role(Role.ROLE_USER)
        .build();

    farm = Farm.builder()
        .user(user)
        .name("테스트 농가 이름1")
        .address("서울")
        .isMain(false)
        .startDate(LocalDate.now())
        .build();

    screen = Screen.builder()
        .farm(farm)
        .imageUrl("test-url")
        .cropCondition(24.5F)
        .measureTime("2021-08-10")
        .build();

    screenResponse = ScreenResponse.of(screen);

  }

  @DisplayName("내 농가 실시간 화면 조회가 성공한다.")
  @Test
  void findLiveScreen() {
    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
    when(iotUtils.getLiveScreen(any())).thenReturn(screen.getMeasureTime());
    when(screenRepository.findByFarmAndMeasureTime(any(), any())).thenReturn(ofNullable(screen));
    when(modelMapper.map(any(), any())).thenReturn(screenResponse);

    screenService.findLiveScreen(user, 1L);

    Assertions.assertAll(
        () -> assertThat(screenResponse.getImageUrl()).isEqualTo(screen.getImageUrl()),
        () -> assertThat(screenResponse.getCropCondition()).isEqualTo(screen.getCropCondition()),
        () -> assertThat(screenResponse.getMeasureTime()).isEqualTo(screen.getMeasureTime())
    );
  }

  @DisplayName("존재하지 않는 농가의 실시간 화면 조회시 예외가 발생한다.")
  @Test
  void findLiveScreen_failure_illegal_farmId() {
    when(farmRepository.findById(any())).thenThrow(new CustomException(ErrorCode.INVALID_FARM_ID));

    assertThatThrownBy(() -> screenService.findLiveScreen(user, 1L))
        .isInstanceOf(CustomException.class)
        .hasMessage(ErrorCode.INVALID_FARM_ID.getMessage());
  }

  @DisplayName("권한이 없는 농가의 실시간 화면 조회시 예외가 발생한다.")
  @Test
  void findLiveScreen_failure_illegal_access() {
    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));

    User anonymous = User.builder().build();

    assertThatThrownBy(() -> screenService.findLiveScreen(anonymous, 2L))
        .isInstanceOf(CustomException.class)
        .hasMessage(ErrorCode.FARM_ACCESS_DENIED.getMessage());
  }

  @DisplayName("iot 연결이 실패하면 예외가 발생한다.")
  @Test
  void findLiveScreen_failure_jsch_error() {
    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
    when(iotUtils.getLiveScreen(any()))
        .thenThrow(new CustomException(ErrorCode.INTERNAL_IOT_SERVER_ERROR));

    assertThatThrownBy(() -> screenService.findLiveScreen(user, 1L))
        .isInstanceOf(CustomException.class)
        .hasMessage(ErrorCode.INTERNAL_IOT_SERVER_ERROR.getMessage());

  }

  @DisplayName("실기간 화면이 저장되지 않았을 경우 조회 시 예외가 발생한다.")
  @Test
  void findLiveScreen_failure_not_exist_screen() {
    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
    when(iotUtils.getLiveScreen(any())).thenReturn(screen.getMeasureTime());
    when(screenRepository.findByFarmAndMeasureTime(any(), any()))
        .thenThrow(new CustomException(ErrorCode.NON_EXISTENT_SCREEN));

    assertThatThrownBy(() -> screenService.findLiveScreen(user, 1L))
        .isInstanceOf(CustomException.class)
        .hasMessage(ErrorCode.NON_EXISTENT_SCREEN.getMessage());
  }

  @DisplayName("IOT에서 요청을 처리하지 못했을 경우 예외가 발생한다.")
  @Test
  void findLiveScreen_failure_iotServer_error() {
    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
    when(iotUtils.getLiveScreen(any())).thenThrow(new CustomException(ErrorCode.INTERNAL_IOT_SERVER_ERROR));

    assertThatThrownBy(() -> screenService.findLiveScreen(user, 1L))
        .isInstanceOf(CustomException.class)
        .hasMessage(ErrorCode.INTERNAL_IOT_SERVER_ERROR.getMessage());
  }

  @DisplayName("오늘 저장된 모든 화면을 조회한다.")
  @Test
  void findTodayScreens() {
    Screen screen2 = Screen.builder()
        .farm(farm)
        .imageUrl("test-url")
        .cropCondition(11.5F)
        .measureTime(LocalDateTime.now().toString())
        .build();

    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
    when(screenRepository
        .findByFarmAndMeasureTimeStartingWith(any(), any())).thenReturn(List.of(screen2));
    when(modelMapper.map(any(), any())).thenReturn(ScreenResponse.of(screen2));

    List<ScreenResponse> responses = screenService.findTodayScreens(user, 1L);

    Assertions.assertAll(
        () -> assertThat(responses.size()).isEqualTo(1),
        () -> assertThat(responses.get(0).getImageUrl()).isEqualTo(screen2.getImageUrl()),
        () -> assertThat(responses.get(0).getCropCondition()).isEqualTo(screen2.getCropCondition()),
        () -> assertThat(responses.get(0).getMeasureTime()).isEqualTo(screen2.getMeasureTime())
    );
  }

  @DisplayName("오늘 저장된 화면이 없을 경우 빈 리스트가 반환된다.")
  @Test
  void findTodayScreens_is_EmptyList() {
    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
    when(screenRepository
        .findByFarmAndMeasureTimeStartingWith(any(), any())).thenReturn(List.of());

    List<ScreenResponse> responses = screenService.findTodayScreens(user, 1L);

    assertThat(responses.size()).isEqualTo(0);
  }
}
