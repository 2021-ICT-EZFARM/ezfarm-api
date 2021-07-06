package com.ezfarm.ezfarmback.remote.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.remote.dto.RemoteResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class RemoteAcceptanceStep {

    public static ExtractableResponse<Response> requestToViewRemote(String url, AuthResponse authResponse) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .get(url + "/remote")
            .then().log().all()
            .extract();
    }

    public static void assertThatFindRemote(ExtractableResponse<Response> response) {
        RemoteResponse remoteResponse = new RemoteResponse();
        remoteResponse.setValues(response.asString());
        assertThat(remoteResponse.getValues())
            .isEqualTo("{tmp: 40, humidity: 40, illuminance: 40, co2: 40, ph: 40, mos: 40}");
    }

    public static ExtractableResponse<Response> requestToUpdateRemote(String url, AuthResponse authResponse, String valeus) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(valeus)
            .when()
            .post(url + "/remote")
            .then().log().all()
            .extract();
    }
}
