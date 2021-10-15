package com.ezfarm.ezfarmback.iot.service;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.common.iot.IotConnector;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.iot.domain.OnOff;
import com.ezfarm.ezfarmback.iot.domain.Remote;
import com.ezfarm.ezfarmback.iot.domain.RemoteRepository;
import com.ezfarm.ezfarmback.iot.domain.Screen;
import com.ezfarm.ezfarmback.iot.domain.ScreenRepository;
import com.ezfarm.ezfarmback.iot.dto.RemoteRequest;
import com.ezfarm.ezfarmback.iot.dto.RemoteResponse;
import com.ezfarm.ezfarmback.iot.dto.ScreenResponse;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import java.util.Optional;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("IOT 단위 테스트(Service)")
public class IotServiceTest {

  @Mock
  FarmRepository farmRepository;

  @Mock
  RemoteRepository remoteRepository;

  @Mock
  ScreenRepository screenRepository;

  @Mock
  IotConnector iotConnector;

  IotService iotService;

  User user;

  Farm farm;

  Remote remote;

  RemoteRequest remoteRequest;

  RemoteResponse remoteResponse;


  @BeforeEach
  void setUp() {
    iotService = new IotService(iotConnector, farmRepository, screenRepository, remoteRepository);

    remoteResponse = RemoteResponse.builder()
        .id(1L)
        .water(OnOff.ON)
        .temperature("37.5")
        .illuminance(OnOff.OFF)
        .co2(OnOff.OFF)
        .build();

    remoteRequest = RemoteRequest.builder()
        .remoteId(1L)
        .water(OnOff.ON.toString())
        .temperature("37.5")
        .illuminance(OnOff.OFF.toString())
        .co2(OnOff.OFF.toString())
        .build();

    user = User.builder()
        .id(1L)
        .email("test@email.com")
        .name("테스트 유저")
        .password("1234")
        .role(Role.ROLE_USER)
        .build();

    farm = Farm.builder()
        .user(user)
        .build();

    remote = Remote.builder()
        .id(1L)
        .farm(farm)
        .co2(OnOff.OFF)
        .illuminance(OnOff.OFF)
        .temperature("40.5")
        .water(OnOff.ON)
        .build();
  }

  @DisplayName("농가 제어값을 조회한다.")
  @Test
  void findRemote() {
    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
    when(remoteRepository.findByFarm(any())).thenReturn(ofNullable(remote));

    RemoteResponse response = iotService.findRemote(1L);

    Assertions.assertAll(
        () -> assertThat(response.getTemperature()).isEqualTo(remote.getTemperature()),
        () -> AssertionsForInterfaceTypes.assertThat(response.getIlluminance())
            .isEqualTo(remote.getIlluminance()),
        () -> AssertionsForInterfaceTypes.assertThat(response.getCo2())
            .isEqualTo(remote.getCo2()),
        () -> AssertionsForInterfaceTypes.assertThat(response.getWater())
            .isEqualTo(remote.getWater())
    );
  }

  @DisplayName("농가 제어값을 생성한 후 조회한다.")
  @Test
  void findRemote_after_Creation() {
    Remote defaultRemote = Remote.create(farm);

    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
    when(remoteRepository.findByFarm(any())).thenReturn(Optional.empty());
    when(remoteRepository.save(any())).thenReturn(defaultRemote);

    RemoteResponse response = iotService.findRemote(1L);

    Assertions.assertAll(
        () -> assertThat(response.getTemperature()).isEqualTo(defaultRemote.getTemperature()),
        () -> AssertionsForInterfaceTypes.assertThat(response.getIlluminance())
            .isEqualTo(defaultRemote.getIlluminance()),
        () -> AssertionsForInterfaceTypes.assertThat(response.getCo2())
            .isEqualTo(defaultRemote.getCo2()),
        () -> AssertionsForInterfaceTypes.assertThat(response.getWater())
            .isEqualTo(defaultRemote.getWater())
    );
  }

  @DisplayName("농가 제어값을 조회 시 존재하지 않는 농가면 예외가 발생한다.")
  @Test
  void findRemote_failure() {
    when(farmRepository.findById(any())).thenReturn(Optional.empty());
    assertThatThrownBy(() -> iotService.findRemote(1L))
        .isInstanceOf(CustomException.class)
        .hasMessage(ErrorCode.INVALID_FARM_ID.getMessage());
  }

  @DisplayName("농가 제어를 요청한다.")
  @Test
  void updateRemote() {
    when(remoteRepository.findById(any())).thenReturn(ofNullable(remote));
    doNothing().when(iotConnector).updateRemote(any());

    iotService.updateRemote(user, remoteRequest);

    Assertions.assertAll(
        () -> assertThat(remote.getTemperature()).isEqualTo(remoteRequest.getTemperature()),
        () -> assertThat(remote.getCo2()).isEqualTo(OnOff.valueOf(remoteRequest.getCo2())),
        () -> assertThat(remote.getWater()).isEqualTo(OnOff.valueOf(remoteRequest.getWater())),
        () -> assertThat(remote.getIlluminance()).isEqualTo(
            OnOff.valueOf(remoteRequest.getIlluminance()))
    );
  }

  @DisplayName("접근 권한이 없는 농가 제어 요청 시 예외가 발생한다.")
  @Test
  void updateRemote_failure_access_denied() {
    User deniedUser = User.builder()
        .id(2L)
        .build();
    when(remoteRepository.findById(any())).thenReturn(ofNullable(remote));
    assertThatThrownBy(() -> iotService.updateRemote(deniedUser, remoteRequest))
        .isInstanceOf(CustomException.class)
        .hasMessage(ErrorCode.FARM_ACCESS_DENIED.getMessage());
  }

  @DisplayName("농가 제어 엔티티가 존재하지 않을 시 예외가 발생한다.")
  @Test
  void updateRemote_failure() {
    when(remoteRepository.findById(any())).thenReturn(Optional.empty());
    assertThatThrownBy(() -> iotService.updateRemote(user, remoteRequest))
        .isInstanceOf(CustomException.class)
        .hasMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
  }

  @DisplayName("내 농가 실시간 화면 조회가 성공한다.")
  @Test
  void findLiveScreen() {
    Screen screen = Screen.builder()
        .farm(farm)
        .imageUrl("image")
        .cropCondition("24.5")
        .measureTime("2")
        .build();
    ScreenResponse response = ScreenResponse.of(screen);

    when(farmRepository.findById(any())).thenReturn(ofNullable(farm));
    when(iotConnector.getLiveScreen(any())).thenReturn(screen.getMeasureTime());
    when(screenRepository.findByFarmAndMeasureTime(any(), any())).thenReturn(ofNullable(screen));

    iotService.findLiveScreen(user, 1L);

    Assertions.assertAll(
        () -> org.assertj.core.api.Assertions.assertThat(response.getImageUrl())
            .isEqualTo(screen.getImageUrl()),
        () -> org.assertj.core.api.Assertions.assertThat(response.getCropCondition())
            .isEqualTo(screen.getCropCondition()),
        () -> org.assertj.core.api.Assertions.assertThat(response.getMeasureTime())
            .isEqualTo(screen.getMeasureTime())
    );
  }
}
