package com.ezfarm.ezfarmback.farm.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.LOCATION;

import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchCond;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;

public class FarmAcceptanceStep {

  public static void assertThatFindMyFarms(List<FarmResponse> farmResponses,
      FarmRequest farmRequest1, FarmRequest farmRequest2) {
    Assertions.assertAll(
        () -> assertThat(farmResponses.size()).isEqualTo(2),
        () -> assertThat(farmResponses).extracting("name")
            .contains(farmRequest1.getName(), farmRequest2.getName()),
        () -> assertThat(farmResponses).extracting("phoneNumber")
            .contains(farmRequest1.getPhoneNumber(), farmRequest2.getPhoneNumber()),
        () -> assertThat(farmResponses).extracting("address")
            .contains(farmRequest1.getAddress(), farmRequest2.getAddress())
    );
  }

  public static void assertThatFindMyFarm(FarmResponse farmResponse, FarmRequest farmRequest) {
    assertAll(
        () -> assertThat(farmResponse.getAddress()).isEqualTo(farmRequest.getAddress()),
        () -> assertThat(farmResponse.getName()).isEqualTo(farmRequest.getName()),
        () -> assertThat(farmResponse.getStartDate()).isEqualTo(farmRequest.getStartDate()),
        () -> assertThat(farmResponse.isMain()).isEqualTo(farmRequest.getIsMain()),
        () -> assertThat(farmResponse.getPhoneNumber()).isEqualTo(farmRequest.getPhoneNumber()),
        () -> assertThat(farmResponse.getArea()).isEqualTo(farmRequest.getArea()),
        () -> assertThat(farmResponse.getFarmType()).isEqualTo(farmRequest.getFarmType()),
        () -> assertThat(farmResponse.getCropType()).isEqualTo(farmRequest.getCropType())
    );
  }

  public static void assertThatFindMyNewFarm(FarmResponse farmResponse, FarmRequest farmRequest) {
    assertAll(
        () -> assertThat(farmResponse.getAddress()).isEqualTo(farmRequest.getAddress()),
        () -> assertThat(farmResponse.getName()).isEqualTo(farmRequest.getName()),
        () -> assertThat(farmResponse.getStartDate()).isEqualTo(farmRequest.getStartDate()),
        () -> assertThat(farmResponse.isMain()).isEqualTo(true),
        () -> assertThat(farmResponse.getPhoneNumber()).isEqualTo(farmRequest.getPhoneNumber()),
        () -> assertThat(farmResponse.getArea()).isEqualTo(farmRequest.getArea()),
        () -> assertThat(farmResponse.getFarmType()).isEqualTo(farmRequest.getFarmType()),
        () -> assertThat(farmResponse.getCropType()).isEqualTo(farmRequest.getCropType())
    );
  }

  public static void assertThatFindOtherFarms(List<FarmSearchResponse> farmSearchResponse,
      FarmRequest farmRequest) {
    assertAll(
        () -> assertThat(farmSearchResponse.size()).isEqualTo(1),
        () -> assertThat(farmSearchResponse).extracting("address")
            .contains(farmRequest.getAddress()),
        () -> assertThat(farmSearchResponse).extracting("name")
            .contains(farmRequest.getName())
    );
  }

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
        .post("/api/farm/me")
        .then().log().all()
        .extract();
  }

  public static Long getLocation(ExtractableResponse<Response> response) {
    String header = response.header(LOCATION);
    String[] split = header.split("/");
    return Long.parseLong(split[split.length - 1]);
  }

  public static Long requestToCreateFarmAndGetLocation(AuthResponse authResponse,
      FarmRequest farmRequest, ObjectMapper objectMapper) throws Exception {
    String location = requestToCreateFarm(authResponse, farmRequest, objectMapper)
        .header(LOCATION);
    String[] split = location.split("/");
    return Long.parseLong(split[split.length - 1]);
  }

  public static ExtractableResponse<Response> requestToUpdateFarm(AuthResponse authResponse,
      FarmRequest farmRequest, ObjectMapper objectMapper, Long farmId) throws Exception {
    return given().log().all()
        .header("Authorization",
            authResponse.getTokenType() + " " + authResponse.getAccessToken())
        .when()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(objectMapper.writeValueAsString(farmRequest))
        .when()
        .patch("/api/farm/me/{farmId}", farmId)
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> requestToFindMyFarms(AuthResponse authResponse) {
    return given().log().all()
        .header("Authorization",
            authResponse.getTokenType() + " " + authResponse.getAccessToken())
        .when()
        .get("/api/farm/me")
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> requestToFindMyFarm(AuthResponse authResponse,
      Long farmId) {
    return given().log().all()
        .header("Authorization",
            authResponse.getTokenType() + " " + authResponse.getAccessToken())
        .when()
        .get("/api/farm/me/{farmId}", farmId)
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> requestToDeleteMyFarm(AuthResponse authResponse,
      Long farmId) {
    return given().log().all()
        .header("Authorization",
            authResponse.getTokenType() + " " + authResponse.getAccessToken())
        .when()
        .delete("/api/farm/me/{farmId}", farmId)
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> requestToFindOtherFarms(AuthResponse authResponse,
      FarmSearchCond cond) {
    String farmGroup = cond.getFarmGroup() != null ? "farmGroup=" + cond.getFarmGroup() + "&" : "";
    String farmType = cond.getFarmType() != null ? "farmType=" + cond.getFarmType() + "&" : "";
    String cropType = cond.getCropType() != null ? "cropType=" + cond.getCropType() + "&" : "";
    return given().log().all()
        .header("Authorization",
            authResponse.getTokenType() + " " + authResponse.getAccessToken())
        .when()
        .get("/api/farm/other?" + farmGroup + farmType + cropType + "page=0&size=10")
        .then().log().all()
        .extract();
  }
}
