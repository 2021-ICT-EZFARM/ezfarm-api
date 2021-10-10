package com.ezfarm.ezfarmback.iot.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.common.db.AcceptanceStep;
import com.ezfarm.ezfarmback.common.db.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.farm.acceptance.step.FarmAcceptanceStep;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.iot.acceptance.step.IotAcceptanceStep;
import com.ezfarm.ezfarmback.iot.domain.OnOff;
import com.ezfarm.ezfarmback.iot.domain.RemoteHistory;
import com.ezfarm.ezfarmback.iot.domain.RemoteHistoryRepository;
import com.ezfarm.ezfarmback.iot.dto.RemoteRequest;
import com.ezfarm.ezfarmback.iot.dto.RemoteResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("IOT 통합 테스트")
public class IotAcceptanceTest extends CommonAcceptanceTest {

  AuthResponse authResponse;

  FarmRequest farmRequest;

  @Autowired
  RemoteHistoryRepository remoteHistoryRepository;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    LoginRequest loginRequest = new LoginRequest("test1@email.com", "비밀번호");
    authResponse = getAuthResponse(loginRequest);
    farmRequest = FarmRequest.builder()
        .name("테스트 농가 이름1")
        .address("서울")
        .phoneNumber("010-1234-1234")
        .area("1000")
        .farmType(FarmType.VINYL.toString())
        .cropType(CropType.TOMATO.toString())
        .isMain(true)
        .build();
  }

  @DisplayName("제어 값을 조회한다.")
  @Test
  void createRemote() {
    Long farmId = FarmAcceptanceStep.requestToCreateFarmAndGetLocation(authResponse, farmRequest);

    ExtractableResponse<Response> response = IotAcceptanceStep.requestToFindRemote(authResponse, farmId);
    RemoteResponse remoteResponse = response.jsonPath().getObject(".", RemoteResponse.class);
    IotAcceptanceStep.assertThatFindRemote(remoteResponse);
  }

  @Disabled
  @DisplayName("제어 값을 수정한다.")
  @Test
  void updateRemote() throws Exception {
    Long farmId = FarmAcceptanceStep
        .requestToCreateFarmAndGetLocation(authResponse, farmRequest);
    Long remoteId = IotAcceptanceStep.requestToFindRemote(authResponse, farmId).jsonPath()
        .getObject(".", RemoteResponse.class).getId();
    RemoteRequest remoteRequest = RemoteRequest.builder()
        .remoteId(remoteId)
        .water(OnOff.ON.toString())
        .temperature("37.5")
        .illuminance(OnOff.OFF.toString())
        .co2(OnOff.OFF.toString())
        .build();

    ExtractableResponse<Response> updateResponse = IotAcceptanceStep
        .requestToUpdateRemote(authResponse, remoteRequest, objectMapper);
    RemoteResponse response = IotAcceptanceStep.requestToFindRemote(authResponse, farmId)
        .jsonPath().getObject(".", RemoteResponse.class);

    AcceptanceStep.assertThatStatusIsOk(updateResponse);
    IotAcceptanceStep.assertThatUpdateRemote(response, remoteRequest);
  }

  @Disabled
  @DisplayName("제어 값을 수정할 경우 히스토리가 저장된다.")
  @Test
  void updateRemoteAndCreateHistory() throws Exception {
    Long farmId = FarmAcceptanceStep.requestToCreateFarmAndGetLocation(authResponse, farmRequest);
    Long remoteId = IotAcceptanceStep.requestToFindRemote(authResponse, farmId).jsonPath()
        .getObject(".", RemoteResponse.class).getId();
    RemoteRequest remoteRequest = RemoteRequest.builder()
        .remoteId(remoteId)
        .water(OnOff.ON.toString())
        .temperature("37.5")
        .illuminance(OnOff.ON.toString())
        .co2(OnOff.ON.toString())
        .build();

    IotAcceptanceStep.requestToUpdateRemote(authResponse, remoteRequest, objectMapper);

    List<RemoteHistory> remoteHistories = remoteHistoryRepository.findAll();
    Assertions.assertAll(
        () -> assertThat(remoteHistories.size()).isEqualTo(3),
        () -> assertThat(remoteHistories.get(0).getValue()).isEqualTo(
            "Water 설정을 OFF에서 ON(으)로 변경하였습니다"),
        () -> assertThat(remoteHistories.get(1).getValue()).isEqualTo(
            "Temperature 설정을 0.0에서 37.5(으)로 변경하였습니다"),
        () -> assertThat(remoteHistories.get(2).getValue()).isEqualTo(
            "Co2 설정을 OFF에서 ON(으)로 변경하였습니다")
    );
  }
}
