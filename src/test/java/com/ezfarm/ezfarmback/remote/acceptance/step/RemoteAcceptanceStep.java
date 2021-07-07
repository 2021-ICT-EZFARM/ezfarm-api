package com.ezfarm.ezfarmback.remote.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ezfarm.ezfarmback.remote.domain.OnOff;
import com.ezfarm.ezfarmback.remote.dto.RemoteRequest;
import com.ezfarm.ezfarmback.remote.dto.RemoteResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;

public class RemoteAcceptanceStep {

    public static void assertThatFindRemote(RemoteResponse response) {
        Assertions.assertAll(
            () -> assertThat(response.getTemperature()).isEqualTo(0.0f),
            () -> AssertionsForInterfaceTypes.assertThat(response.getIlluminance())
                .isEqualTo(OnOff.OFF),
            () -> AssertionsForInterfaceTypes.assertThat(response.getCo2()).isEqualTo(OnOff.OFF),
            () -> AssertionsForInterfaceTypes.assertThat(response.getWater()).isEqualTo(OnOff.OFF)
        );
    }

    public static void assertThatUpdateRemote(RemoteResponse response,
        RemoteRequest remoteRequest) {
        Assertions.assertAll(
            () -> assertThat(response.getTemperature()).isEqualTo(remoteRequest.getTemperature()),
            () -> AssertionsForInterfaceTypes.assertThat(response.getIlluminance())
                .isEqualTo(remoteRequest.getIlluminance()),
            () -> AssertionsForInterfaceTypes.assertThat(response.getCo2())
                .isEqualTo(remoteRequest.getCo2()),
            () -> AssertionsForInterfaceTypes.assertThat(response.getWater())
                .isEqualTo(remoteRequest.getWater())
        );
    }

    public static ExtractableResponse<Response> requestToFindRemote(AuthResponse authResponse,
        Long farmId) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .get(String.format("/api/remote?farmId=%d", farmId))
            .then().log().all()
            .extract();
    }

    public static void requestToUpdateRemote(AuthResponse authResponse,
        RemoteRequest remoteRequest, ObjectMapper objectMapper) throws Exception {
        given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(objectMapper.writeValueAsString(remoteRequest))
            .patch("/api/remote")
            .then().log().all()
            .extract();
    }
}
