package com.ezfarm.ezfarmback.farm.acceptance;


import static com.ezfarm.ezfarmback.common.acceptance.AcceptanceStep.assertThatStatusIsOk;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ezfarm.ezfarmback.common.acceptance.AcceptanceStep;
import com.ezfarm.ezfarmback.common.acceptance.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.farm.acceptance.step.FarmAcceptanceStep;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("농가 통합 테스트")
public class FarmAcceptanceTest extends CommonAcceptanceTest {

    FarmRequest farmRequest1;

    FarmRequest farmRequest2;

    AuthResponse authResponse;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        LoginRequest loginRequest = new LoginRequest("test1@email.com", "비밀번호");
        authResponse = getAuthResponse(loginRequest);

        farmRequest1 = FarmRequest.builder()
            .name("테스트 농장 이름1")
            .address("서울")
            .phoneNumber("010-1234-1234")
            .isMain(false)
            .build();

        farmRequest2 = FarmRequest.builder()
            .name("테스트 농장 이름2")
            .address("경기")
            .phoneNumber("010-1234-1234")
            .isMain(false)
            .build();
    }

    @DisplayName("나의 농가를 생성한다.")
    @Test
    void createFarm() throws Exception {
        ExtractableResponse<Response> createResponse = FarmAcceptanceStep
            .requestToCreateFarm(authResponse, farmRequest1, objectMapper);
        Long farmId = FarmAcceptanceStep.getLocation(createResponse);

        ExtractableResponse<Response> findResponse = FarmAcceptanceStep
            .requestToFindMyFarm(authResponse, farmId);
        FarmResponse farmResponse = findResponse.jsonPath().getObject(".", FarmResponse.class);

        AcceptanceStep.assertThatStatusIsCreated(createResponse);
        FarmAcceptanceStep.assertThatFindMyFarm(farmResponse, farmRequest1);
    }

    @DisplayName("나의 농가를 수정한다.")
    @Test
    void updateFarm() throws Exception {
        FarmRequest updateRequest = FarmRequest.builder()
            .name("테스트 농장 이름3")
            .address("경기")
            .isMain(true)
            .build();

        Long farmId = FarmAcceptanceStep
            .requestToCreateFarmAndGetLocation(authResponse, farmRequest1, objectMapper);
        ExtractableResponse<Response> updateResponse = FarmAcceptanceStep
            .requestToUpdateFarm(authResponse, updateRequest, objectMapper, farmId);

        ExtractableResponse<Response> findResponse = FarmAcceptanceStep
            .requestToFindMyFarm(authResponse, farmId);
        FarmResponse farmResponse = findResponse.jsonPath().getObject(".", FarmResponse.class);

        assertThatStatusIsOk(updateResponse);
        FarmAcceptanceStep.assertThatFindMyFarm(farmResponse, updateRequest);
    }

    @DisplayName("나의 모든 농가를 조회한다.")
    @Test
    void findMyFarms() throws Exception {

        FarmAcceptanceStep.requestToCreateFarm(authResponse, farmRequest1, objectMapper);
        FarmAcceptanceStep.requestToCreateFarm(authResponse, farmRequest2, objectMapper);

        ExtractableResponse<Response> response = FarmAcceptanceStep
            .requestToFindMyFarms(authResponse);
        List<FarmResponse> farmResponses = response.jsonPath().getList(".", FarmResponse.class);

        assertThatStatusIsOk(response);
        FarmAcceptanceStep.assertThatFindMyFarms(farmResponses, farmRequest1, farmRequest2);
    }

    @DisplayName("나의 농가를 조회한다.")
    @Test
    void findMyFarm() throws Exception {
        Long farmId = FarmAcceptanceStep
            .requestToCreateFarmAndGetLocation(authResponse, farmRequest1, objectMapper);
        ExtractableResponse<Response> response = FarmAcceptanceStep
            .requestToFindMyFarm(authResponse, farmId);
        FarmResponse farmResponse = response.jsonPath().getObject(".", FarmResponse.class);

        AcceptanceStep.assertThatStatusIsOk(response);
        FarmAcceptanceStep.assertThatFindMyFarm(farmResponse, farmRequest1);
    }

    @DisplayName("농가를 삭제한다.")
    @Test
    void deleteFarm() throws Exception {
        Long farmId = FarmAcceptanceStep
            .requestToCreateFarmAndGetLocation(authResponse, farmRequest1, objectMapper);

        ExtractableResponse<Response> deleteResponse = FarmAcceptanceStep
            .requestToDeleteMyFarm(authResponse, farmId);

        List<FarmResponse> farmResponses = FarmAcceptanceStep.requestToFindMyFarms(authResponse)
            .jsonPath()
            .getList(".", FarmResponse.class);

        AcceptanceStep.assertThatStatusIsNoContent(deleteResponse);
        assertThat(farmResponses.size()).isEqualTo(0);
    }
}
