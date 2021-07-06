package com.ezfarm.ezfarmback.farm.acceptance.step;

import static io.restassured.RestAssured.given;

import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class FarmAcceptanceStep {

    public static ExtractableResponse<Response> requestToCreateFarm(
        AuthResponse authResponse,
        FarmRequest farmRequest, ObjectMapper objectMapper)
        throws Exception {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(objectMapper.writeValueAsString(farmRequest))
            .when()
            .post("/api/farm")
            .then().log().all()
            .extract();
    }

    public static Long getLocation(ExtractableResponse<Response> response) {
        String header = response.header(HttpHeaders.LOCATION);
        String[] split = header.split("/");
        return Long.parseLong(split[split.length - 1]);
    }
}
