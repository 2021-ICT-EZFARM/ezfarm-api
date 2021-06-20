package com.ezfarm.ezfarmback.farm.acceptance;


import static io.restassured.RestAssured.given;

import com.ezfarm.ezfarmback.common.AcceptanceStep;
import com.ezfarm.ezfarmback.common.CommonSecurityTest;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("농장 통합 테스트")
public class FarmAcceptanceTest extends CommonSecurityTest {

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

}
