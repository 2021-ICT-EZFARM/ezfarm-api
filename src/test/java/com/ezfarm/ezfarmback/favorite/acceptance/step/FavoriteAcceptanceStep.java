package com.ezfarm.ezfarmback.favorite.acceptance.step;

import static io.restassured.RestAssured.given;

import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class FavoriteAcceptanceStep {

    public static ExtractableResponse<Response> requestToAddFavorite(AuthResponse authResponse,
        Long farmId) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/api/favorite/{farmId}", farmId)
            .then().log().all()
            .extract();
    }
}
