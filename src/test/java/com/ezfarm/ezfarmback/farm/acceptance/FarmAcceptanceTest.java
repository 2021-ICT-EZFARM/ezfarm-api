package com.ezfarm.ezfarmback.farm.acceptance;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import com.ezfarm.ezfarmback.common.acceptance.AcceptanceStep;
import com.ezfarm.ezfarmback.common.acceptance.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovyjarjarasm.asm.Type;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("농장 통합 테스트")
public class FarmAcceptanceTest extends CommonAcceptanceTest {

    @Autowired
    FarmRepository farmRepository;


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

    @DisplayName("modelmapper가 제대로 매핑하는지 테스트한다.(FarmRequest -> Farm)")
    @Test
    void testModelMapper() throws JsonProcessingException {
        FarmRequest farmRequest = new FarmRequest("서울시", "010-0000-0000",
            "100평", true, FarmType.GLASS, CropType.PAPRIKA, LocalDate.now());

        //when
        ExtractableResponse<Response> response = given().log().all()
            .header("Authorization", authenticationToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(objectMapper.writeValueAsString(farmRequest))
            .when()
            .post("/api/farm")
            .then().log().all()
            .extract();

        //then
        Farm newFarm = farmRepository.findById(1L).get();
        assertEquals(newFarm.getAddress(), "서울시");
        assertEquals(newFarm.getPhoneNumber(), "010-0000-0000");
        assertEquals(newFarm.getArea(), "100평");
        assertEquals(newFarm.getFarmType(), FarmType.GLASS);
        assertEquals(newFarm.getCropType(), CropType.PAPRIKA);
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
