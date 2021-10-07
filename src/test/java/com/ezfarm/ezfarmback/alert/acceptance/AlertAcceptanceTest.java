package com.ezfarm.ezfarmback.alert.acceptance;

import com.ezfarm.ezfarmback.alert.acceptance.step.AlertAcceptanceStep;
import com.ezfarm.ezfarmback.alert.dto.AlertRangeRequest;
import com.ezfarm.ezfarmback.alert.dto.AlertRangeResponse;
import com.ezfarm.ezfarmback.common.db.AcceptanceStep;
import com.ezfarm.ezfarmback.common.db.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.farm.acceptance.step.FarmAcceptanceStep;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("알림 통합 테스트")
public class AlertAcceptanceTest extends CommonAcceptanceTest {

    AuthResponse authResponse;

    FarmRequest farmRequest;

    AlertRangeRequest alertRangeRequest;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        LoginRequest loginRequest = new LoginRequest("test1@email.com", "비밀번호");
        authResponse = getAuthResponse(loginRequest);

        farmRequest = new FarmRequest(
            "경기",
            "테스트 이름",
            "010-2222-2222",
            "100",
            true,
            FarmType.GLASS.toString(),
            CropType.PAPRIKA.toString(),
            LocalDate.now()
        );

        alertRangeRequest = new AlertRangeRequest();
    }

    @DisplayName("알림 범위를 생성한 후 조회한다.")
    @Test
    void findAlertRange_after_creation() throws Exception {
        ExtractableResponse<Response> farmResponse = FarmAcceptanceStep
            .requestToCreateFarm(authResponse, farmRequest);
        Long farmId = FarmAcceptanceStep.getLocation(farmResponse);

        ExtractableResponse<Response> response = AlertAcceptanceStep
            .requestToFindAlertRange(authResponse, farmId);
        AlertRangeResponse alertRangeResponse = response.jsonPath()
            .getObject(".", AlertRangeResponse.class);

        AcceptanceStep.assertThatStatusIsOk(response);
        AlertAcceptanceStep.assertThatFindAlertRange(alertRangeResponse, alertRangeRequest);
    }

    @DisplayName("알림 범위를 수정한다.")
    @Test
    void updateAlertRange() throws Exception {
        //given
        alertRangeRequest.setTmpMin(10.1f);
        alertRangeRequest.setHumidityMax(20.6f);

        //when
        ExtractableResponse<Response> farmResponse = FarmAcceptanceStep
            .requestToCreateFarm(authResponse, farmRequest);
        Long farmId = FarmAcceptanceStep.getLocation(farmResponse);

        Long alertRangeId = AlertAcceptanceStep
            .requestToFindAlertRange(authResponse, farmId).jsonPath()
            .getObject(".", AlertRangeResponse.class)
            .getId();
        ExtractableResponse<Response> response = AlertAcceptanceStep
            .requestToUpdateAlertRange(authResponse, alertRangeRequest, alertRangeId, objectMapper);

        AlertRangeResponse alertRangeResponse = AlertAcceptanceStep
            .requestToFindAlertRange(authResponse, farmId).jsonPath()
            .getObject(".", AlertRangeResponse.class);

        //then
        AcceptanceStep.assertThatStatusIsOk(response);
        AlertAcceptanceStep.assertThatFindAlertRange(alertRangeResponse, alertRangeRequest);
    }
}
