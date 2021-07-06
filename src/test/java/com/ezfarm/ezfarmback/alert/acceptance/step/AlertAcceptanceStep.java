package com.ezfarm.ezfarmback.alert.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.alert.dto.AlertRangeRequest;
import com.ezfarm.ezfarmback.alert.dto.AlertRangeResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;

public class AlertAcceptanceStep {

    public static void assertThatFindAlertRange(AlertRangeResponse alertRangeResponse
        , AlertRangeRequest alertRangeRequest) {
        Assertions.assertAll(
            () -> assertThat(alertRangeResponse.getCo2Max())
                .isEqualTo(alertRangeRequest.getCo2Max()),
            () -> assertThat(alertRangeResponse.getCo2Min())
                .isEqualTo(alertRangeRequest.getCo2Min()),
            () -> assertThat(alertRangeResponse.getHumidityMax())
                .isEqualTo(alertRangeRequest.getHumidityMax()),
            () -> assertThat(alertRangeResponse.getHumidityMin())
                .isEqualTo(alertRangeRequest.getHumidityMin()),
            () -> assertThat(alertRangeResponse.getImnMax())
                .isEqualTo(alertRangeRequest.getImnMax()),
            () -> assertThat(alertRangeResponse.getImnMin())
                .isEqualTo(alertRangeRequest.getImnMin()),
            () -> assertThat(alertRangeResponse.getMosMax())
                .isEqualTo(alertRangeRequest.getMosMax()),
            () -> assertThat(alertRangeResponse.getMosMin())
                .isEqualTo(alertRangeRequest.getMosMin()),
            () -> assertThat(alertRangeResponse.getPhMax())
                .isEqualTo(alertRangeRequest.getPhMax()),
            () -> assertThat(alertRangeResponse.getPhMin())
                .isEqualTo(alertRangeRequest.getPhMin()),
            () -> assertThat(alertRangeResponse.getTmpMax())
                .isEqualTo(alertRangeRequest.getTmpMax()),
            () -> assertThat(alertRangeResponse.getTmpMin())
                .isEqualTo(alertRangeRequest.getTmpMin())
        );
    }

    public static ExtractableResponse<Response> requestToFindAlertRange(AuthResponse authResponse,
        Long farmId) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .get(String.format("/api/alert/alert-range?farmId=%d", farmId))
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> requestToUpdateAlertRange(AuthResponse authResponse,
        AlertRangeRequest alertRangeRequest, Long alertRangeId, ObjectMapper objectMapper)
        throws Exception {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(objectMapper.writeValueAsString(alertRangeRequest))
            .patch(String.format("/api/alert/alert-range?alertRangeId=%d", alertRangeId))
            .then().log().all()
            .extract();
    }
}
