package com.ezfarm.ezfarmback.farm.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.LOCATION;

import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.farm.dto.FarmResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
        String header = response.header(LOCATION);
        String[] split = header.split("/");
        return Long.parseLong(split[split.length - 1]);
    }

    public static ExtractableResponse<Response> requestToUpdateFarm(String url, AuthResponse authResponse,
        FarmRequest farmRequest, ObjectMapper objectMapper) throws Exception {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(objectMapper.writeValueAsString(farmRequest))
            .when()
            .patch(url)
            .then().log().all()
            .extract();
    }

    public static String requestToCreateFarmAndGetLocation(AuthResponse authResponse,
        FarmRequest farmRequest, ObjectMapper objectMapper) throws Exception {
        return requestToCreateFarm(authResponse, farmRequest, objectMapper).header(LOCATION);
    }

    public static ExtractableResponse<Response> requestToFindFarms(AuthResponse authResponse) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .get("/api/farm")
            .then().log().all()
            .extract();
    }

    public static void assertThatFindTwoFarms(ExtractableResponse<Response> response) {
        List<FarmResponse> farmResponses = response.jsonPath().getList(".", FarmResponse.class);

        assertThat(farmResponses).hasSize(2);
    }

    public static ExtractableResponse<Response> requestToFindFarm(String url, AuthResponse authResponse) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .get(url)
            .then().log().all()
            .extract();
    }

    public static void assertThatFindFarm(ExtractableResponse<Response> response) {
        FarmResponse farmResponse = response.as(FarmResponse.class);
        assertAll(
            () -> assertThat(farmResponse.getAddress()).isEqualTo("경기"),
            () -> assertThat(farmResponse.getName()).isEqualTo("테스트 이름"),
            () -> assertThat(farmResponse.getPhoneNumber()).isEqualTo("010-2222-2222"),
            () -> assertThat(farmResponse.getArea()).isEqualTo("100"),
            () -> assertThat(farmResponse.getFarmType()).isEqualTo(FarmType.GLASS),
            () -> assertThat(farmResponse.getCropType()).isEqualTo(CropType.PAPRIKA)
        );

    }

    public static ExtractableResponse<Response> requestToDeleteFarm(String url,
        AuthResponse authResponse) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .delete(url)
            .then().log().all()
            .extract();
    }

    public static void assertThatStatusIsNoContent(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
