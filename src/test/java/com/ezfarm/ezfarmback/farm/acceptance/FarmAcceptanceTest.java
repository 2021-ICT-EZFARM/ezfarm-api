package com.ezfarm.ezfarmback.farm.acceptance;

<<<<<<< Updated upstream

import static io.restassured.RestAssured.given;
=======
import static com.ezfarm.ezfarmback.common.acceptance.AcceptanceStep.assertThatStatusIsOk;
import static com.ezfarm.ezfarmback.farm.acceptance.step.FarmAcceptanceStep.assertThatFindTwoFarms;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
>>>>>>> Stashed changes

import com.ezfarm.ezfarmback.common.acceptance.AcceptanceStep;
import com.ezfarm.ezfarmback.common.acceptance.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("농장 통합 테스트")
public class FarmAcceptanceTest extends CommonAcceptanceTest {

    String authenticationToken;

    FarmRequest farmRequest;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        LoginRequest loginRequest = new LoginRequest("test@email.com", "비밀번호");

        AuthResponse authResponse = getAuthResponse(loginRequest);
        authenticationToken = authResponse.getTokenType() + " " + authResponse.getAccessToken();

        farmRequest = new FarmRequest(
            "경기",
            "010-2222-2222",
            "100",
            true,
            FarmType.GLASS,
            CropType.PAPRIKA,
            LocalDate.now()
        );
    }

    @DisplayName("농장을 생성한다.")
    @Test
    void createFarm() throws JsonProcessingException {
        //when
        ExtractableResponse<Response> response = given().log().all()
            .header("Authorization", authenticationToken)
            .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(objectMapper.writeValueAsString(farmRequest))
            .when()
            .post("/api/farm")
            .then().log().all()
            .extract();

        //then
        AcceptanceStep.assertThatStatusIsCreated(response);
    }

<<<<<<< Updated upstream
=======
    @DisplayName("농가를 수정한다.")
    @Test
    void updateFarm() throws Exception {
        FarmRequest updateRequest = new FarmRequest(
            "서울",
            "테스트 이름",
            "010-3333-3333",
            "200",
            false,
            FarmType.VINYL,
            CropType.PAPRIKA,
            LocalDate.now().plusDays(1)
        );

        String url = FarmAcceptanceStep
            .requestToCreateFarmAndGetLocation(authResponse, farmRequest, objectMapper);
        ExtractableResponse<Response> response = FarmAcceptanceStep
            .requestToUpdateFarm(url, authResponse, updateRequest, objectMapper);

        assertThatStatusIsOk(response);
    }

    @DisplayName("모든 농가를 조회한다.")
    @Test
    void viewAllFarm() throws Exception {
        FarmRequest updateRequest = new FarmRequest(
            "서울",
            "테스트 이름",
            "010-3333-3333",
            "200",
            false,
            FarmType.VINYL,
            CropType.PAPRIKA,
            LocalDate.now().plusDays(1)
        );

        FarmAcceptanceStep.requestToCreateFarm(authResponse, farmRequest, objectMapper);
        FarmAcceptanceStep.requestToCreateFarm(authResponse, updateRequest, objectMapper);
        ExtractableResponse<Response> response = FarmAcceptanceStep
            .requestToFindFarms(authResponse);

        assertThatStatusIsOk(response);
        FarmAcceptanceStep.assertThatFindTwoFarms(response);
    }

    @DisplayName("농가를 조회한다.")
    @Test
    void viweFarm() throws Exception {
        String url = FarmAcceptanceStep
            .requestToCreateFarmAndGetLocation(authResponse, farmRequest, objectMapper);
        ExtractableResponse<Response> response = FarmAcceptanceStep
            .requestToFindFarm(url, authResponse);

        FarmAcceptanceStep.assertThatFindFarm(response);
    }

    @DisplayName("농가를 삭제한다.")
    @Test
    void deleteFarm() throws Exception {
        String url = FarmAcceptanceStep
            .requestToCreateFarmAndGetLocation(authResponse, farmRequest, objectMapper);
        ExtractableResponse<Response> response = FarmAcceptanceStep.requestToDeleteFarm(url, authResponse);

        FarmAcceptanceStep.assertThatStatussNoContent(response);
    }
>>>>>>> Stashed changes
}
