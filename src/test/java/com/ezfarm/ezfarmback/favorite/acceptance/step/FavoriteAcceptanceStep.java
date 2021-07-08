package com.ezfarm.ezfarmback.favorite.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.favorite.dto.FavoriteResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Assertions;

public class FavoriteAcceptanceStep {

    public static void assertThatFindFavorites(List<FavoriteResponse> favoriteResponses,
        LoginRequest ownerLoginRequest, FarmRequest farmRequest) {
        Assertions.assertAll(
            () -> assertThat(favoriteResponses.size()).isEqualTo(1),
            () -> assertThat(favoriteResponses.get(0).getFarmOwnerResponse().getEmail())
                .isEqualTo(ownerLoginRequest.getEmail()),
            () -> assertThat(favoriteResponses.get(0).getFarmResponse().getName())
                .isEqualTo(farmRequest.getName())
        );
    }

    public static ExtractableResponse<Response> requestToAddFavorite(AuthResponse authResponse,
        Long farmId) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .post("/api/favorite/{farmId}", farmId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> requestToFindFavorite(AuthResponse authResponse) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .get("/api/favorite")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> requestToDeleteFavorite(AuthResponse authResponse,
        Long favoriteId) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .delete(String.format("/api/favorite?favoriteId=%d", favoriteId))
            .then().log().all()
            .extract();
    }
}
