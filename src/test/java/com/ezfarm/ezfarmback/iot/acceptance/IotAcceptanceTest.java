package com.ezfarm.ezfarmback.iot.acceptance;

import com.ezfarm.ezfarmback.common.db.AcceptanceStep;
import com.ezfarm.ezfarmback.common.db.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.farm.acceptance.step.FarmAcceptanceStep;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.iot.acceptance.step.IotAcceptanceStep;
import com.ezfarm.ezfarmback.iot.domain.OnOff;
import com.ezfarm.ezfarmback.iot.dto.RemoteRequest;
import com.ezfarm.ezfarmback.iot.dto.RemoteResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("IOT 통합 테스트")
public class IotAcceptanceTest extends CommonAcceptanceTest {

  AuthResponse authResponse;

  FarmRequest farmRequest;

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

    ExtractableResponse<Response> response = IotAcceptanceStep.requestToFindRemote(authResponse,
        farmId);
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
}
