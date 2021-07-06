package com.ezfarm.ezfarmback.remote.acceptance;

import static com.ezfarm.ezfarmback.common.acceptance.AcceptanceStep.assertThatStatusIsOk;

import com.ezfarm.ezfarmback.common.acceptance.AcceptanceStep;
import com.ezfarm.ezfarmback.common.acceptance.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.farm.acceptance.step.FarmAcceptanceStep;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.remote.acceptance.step.RemoteAcceptanceStep;
import com.ezfarm.ezfarmback.remote.dto.RemoteRequest;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("제어 통합 테스트")
public class RemoteAcceptanceTest extends CommonAcceptanceTest {

    RemoteRequest remoteRequest;

    AuthResponse authResponse;

    String jsonString;

    FarmRequest farmRequest;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        LoginRequest loginRequest = new LoginRequest("test1@email.com", "비밀번호");
        authResponse = getAuthResponse(loginRequest);

        jsonString = "{tmp: 30, humidity: 30, illuminance: 30, co2: 30, ph: 30, mos: 30}";
        remoteRequest = new RemoteRequest(jsonString);

        farmRequest = new FarmRequest(
            "경기",
            "테스트 이름",
            "010-2222-2222",
            "100",
            true,
            FarmType.GLASS,
            CropType.PAPRIKA,
            LocalDate.now()
        );
    }

    @DisplayName("제어 값을 조회한다.")
    @Test
    void createRemote() {
        String url = FarmAcceptanceStep
            .requestToCreateFarmAndGetLocation(authResponse, farmRequest, objectMapper);
        ExtractableResponse<Response> response = RemoteAcceptanceStep
            .requestToViewRemote(url, authResponse);

        RemoteAcceptanceStep.assertThatFindRemote(response);
    }

    @DisplayName("제어 값을 수정한다.")
    @Test
    void updateRemote() {
        String updateJsonString = "{tmp: 40, humidity: 40, illuminance: 40, co2: 40, ph: 40, mos: 40}";
        String url = FarmAcceptanceStep
            .requestToCreateFarmAndGetLocation(authResponse, farmRequest, objectMapper);
        ExtractableResponse<Response> response = RemoteAcceptanceStep
            .requestToUpdateRemote(url, authResponse, updateJsonString);

        assertThatStatusIsOk(response);
    }
}
