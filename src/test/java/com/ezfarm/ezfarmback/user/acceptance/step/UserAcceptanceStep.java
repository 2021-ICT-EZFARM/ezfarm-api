package com.ezfarm.ezfarmback.user.acceptance.step;

import static io.restassured.RestAssured.given;

import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class UserAcceptanceStep {

    public static ExtractableResponse<Response> requestToReadUser(
        AuthResponse authResponse) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .get("/api/user")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> requestToUpdateUser(AuthResponse authResponse,
        UserUpdateRequest userUpdateRequest, ObjectMapper objectMapper)
        throws Exception {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(objectMapper.writeValueAsString(userUpdateRequest))
            .when()
            .patch("/api/user")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> requestToDeleteUser(
        AuthResponse authResponse) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete("/api/user")
            .then().log().all()
            .extract();
    }
}
