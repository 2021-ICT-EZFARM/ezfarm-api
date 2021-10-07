package com.ezfarm.ezfarmback.farm.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.LOCATION;

import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchCond;
import com.ezfarm.ezfarmback.farm.dto.FarmSearchResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
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
        () -> assertThat(farmResponse.isMain()).isEqualTo(farmRequest.isMain()),
        () -> assertThat(farmResponse.getPhoneNumber()).isEqualTo(farmRequest.getPhoneNumber()),
        () -> assertThat(farmResponse.getArea()).isEqualTo(farmRequest.getArea()),
        () -> assertThat(farmResponse.getFarmType()).isEqualTo(
            FarmType.valueOf(farmRequest.getFarmType()).getName()),
        () -> assertThat(farmResponse.getCropType()).isEqualTo(
            CropType.valueOf(farmRequest.getCropType()).getName())
    );
  }

  public static void assertThatFindOtherFarms(List<FarmSearchResponse> responses,
      FarmRequest request) {
    assertAll(
        () -> assertThat(responses.size()).isEqualTo(1),
        () -> assertThat(responses).extracting("address").contains(request.getAddress()),
        () -> assertThat(responses).extracting("name").contains(request.getName())
    );
  }

  public static ExtractableResponse<Response> requestToCreateFarm(AuthResponse response,
      FarmRequest request) {
    return given().log().all()
        .header("Authorization",
            response.getTokenType() + " " + response.getAccessToken())
        .when()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(request)
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
      FarmRequest farmRequest) {
    String location = requestToCreateFarm(authResponse, farmRequest)
        .header(LOCATION);
    String[] split = location.split("/");
    return Long.parseLong(split[split.length - 1]);
  }

  public static ExtractableResponse<Response> requestToUpdateFarm(AuthResponse response,
      FarmRequest request, Long farmId) {
    return given().log().all()
        .header("Authorization",
            response.getTokenType() + " " + response.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(request)
        .when()
        .patch("/api/farm/me/{farmId}", farmId)
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> requestToFindMyFarms(AuthResponse response) {
    return given().log().all()
        .header("Authorization",
            response.getTokenType() + " " + response.getAccessToken())
        .when()
        .get("/api/farm/me")
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> requestToFindMyFarm(AuthResponse response,
      Long farmId) {
    return given().log().all()
        .header("Authorization",
            response.getTokenType() + " " + response.getAccessToken())
        .when()
        .get("/api/farm/me/{farmId}", farmId)
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> requestToDeleteMyFarm(AuthResponse response,
      Long farmId) {
    return given().log().all()
        .header("Authorization",
            response.getTokenType() + " " + response.getAccessToken())
        .when()
        .delete("/api/farm/me/{farmId}", farmId)
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> requestToFindOtherFarms(AuthResponse response,
      FarmSearchCond cond) {
    String farmGroup = cond.getFarmGroup() != null ? "farmGroup=" + cond.getFarmGroup() + "&" : "";
    String farmType = cond.getFarmType() != null ? "farmType=" + cond.getFarmType() + "&" : "";
    String cropType = cond.getCropType() != null ? "cropType=" + cond.getCropType() + "&" : "";
    return given().log().all()
        .header("Authorization",
            response.getTokenType() + " " + response.getAccessToken())
        .when()
        .get("/api/farm/other?" + farmGroup + farmType + cropType + "page=0&size=10")
        .then().log().all()
        .extract();
  }
}
