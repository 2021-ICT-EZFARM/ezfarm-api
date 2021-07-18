package com.ezfarm.ezfarmback.remote.acceptance;

import com.ezfarm.ezfarmback.common.db.AcceptanceStep;
import com.ezfarm.ezfarmback.common.db.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.farm.acceptance.step.FarmAcceptanceStep;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.remote.acceptance.step.RemoteAcceptanceStep;
import com.ezfarm.ezfarmback.remote.domain.OnOff;
import com.ezfarm.ezfarmback.remote.dto.RemoteRequest;
import com.ezfarm.ezfarmback.remote.dto.RemoteResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("제어 통합 테스트")
public class RemoteAcceptanceTest extends CommonAcceptanceTest {

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
            .isMain(false)
            .build();
    }

    @DisplayName("제어 값을 조회한다.")
    @Test
    void createRemote() throws Exception {
        Long farmId = FarmAcceptanceStep
            .requestToCreateFarmAndGetLocation(authResponse, farmRequest, objectMapper);

        ExtractableResponse<Response> response = RemoteAcceptanceStep
            .requestToFindRemote(authResponse, farmId);
        RemoteResponse remoteResponse = response.jsonPath()
            .getObject(".", RemoteResponse.class);

        RemoteAcceptanceStep.assertThatFindRemote(remoteResponse);
    }

    @DisplayName("제어 값을 수정한다.")
    @Test
    void updateRemote() throws Exception {
        Long farmId = FarmAcceptanceStep
            .requestToCreateFarmAndGetLocation(authResponse, farmRequest, objectMapper);
        Long remoteId = RemoteAcceptanceStep.requestToFindRemote(authResponse, farmId).jsonPath()
            .getObject(".", RemoteResponse.class).getId();

        RemoteRequest remoteRequest = new RemoteRequest(remoteId, OnOff.ON, 37.5f, OnOff.OFF,
            OnOff.ON);
        ExtractableResponse<Response> updateResponse = RemoteAcceptanceStep
            .requestToUpdateRemote(authResponse, remoteRequest, objectMapper);
        RemoteResponse response = RemoteAcceptanceStep.requestToFindRemote(authResponse, farmId)
            .jsonPath().getObject(".", RemoteResponse.class);

        AcceptanceStep.assertThatStatusIsOk(updateResponse);
        RemoteAcceptanceStep.assertThatUpdateRemote(response, remoteRequest);
    }
}
