package com.ezfarm.ezfarmback.facility.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.facility.domain.hour.Facility;
import com.ezfarm.ezfarmback.facility.dto.FacilityResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

public class FacilityAcceptanceStep {

  public static ExtractableResponse<Response> requestToFindMainFarmFacility(AuthResponse authResponse) {
    return given().log().all()
        .header("Authorization",
            authResponse.getTokenType() + " " + authResponse.getAccessToken())
        .when()
        .get(String.format("/api/facility"))
        .then().log().all()
        .extract();

  }

  public static void assertThatFindMainFarmFacility(FacilityResponse facilityResponse, Facility facility) {
    Assertions.assertAll(
        () -> assertThat(facilityResponse.getTmp()).isEqualTo(Float.toString(facility.getTmp())),
        () -> assertThat(facilityResponse.getHumidity()).isEqualTo(Float.toString(facility.getHumidity())),
        () -> assertThat(facilityResponse.getIlluminance()).isEqualTo(Float.toString(facility.getIlluminance())),
        () -> assertThat(facilityResponse.getCo2()).isEqualTo(Float.toString(facility.getCo2())),
        () -> assertThat(facilityResponse.getPh()).isEqualTo(Float.toString(facility.getPh())),
        () -> assertThat(facilityResponse.getMos()).isEqualTo(Float.toString(facility.getMos())),
        () -> assertThat(facilityResponse.getMeasureDate()).isEqualTo(facility.getMeasureDate().toString())
    );
  }
}
