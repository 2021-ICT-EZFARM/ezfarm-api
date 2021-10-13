package com.ezfarm.ezfarmback.iot.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ezfarm.ezfarmback.iot.domain.OnOff;
import com.ezfarm.ezfarmback.iot.dto.RemoteRequest;
import com.ezfarm.ezfarmback.iot.dto.RemoteResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;

public class IotAcceptanceStep {

  public static void assertThatFindRemote(RemoteResponse response) {
    Assertions.assertAll(
        () -> assertThat(response.getTemperature()).isEqualTo("0.0"),
        () -> assertThat(response.getIlluminance()).isEqualTo(OnOff.OFF),
        () -> assertThat(response.getCo2()).isEqualTo(OnOff.OFF),
        () -> assertThat(response.getWater()).isEqualTo(OnOff.OFF)
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
        .get(String.format("/api/iot/remote?farmId=%d", farmId))
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> requestToUpdateRemote(AuthResponse authResponse,
      RemoteRequest remoteRequest, ObjectMapper objectMapper) throws Exception {
    return given().log().all()
        .header("Authorization",
            authResponse.getTokenType() + " " + authResponse.getAccessToken())
        .when()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(objectMapper.writeValueAsString(remoteRequest))
        .patch("/api/iot/remote")
        .then().log().all()
        .extract();
  }
}
