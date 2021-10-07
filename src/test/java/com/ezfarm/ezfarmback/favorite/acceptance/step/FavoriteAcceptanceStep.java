package com.ezfarm.ezfarmback.favorite.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.favorite.dto.FavoriteRequest;
import com.ezfarm.ezfarmback.favorite.dto.FavoriteResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;

public class FavoriteAcceptanceStep {

  public static void assertThatFindFavorites(List<FavoriteResponse> responses,
      FarmRequest request) {
    Assertions.assertAll(
        () -> assertThat(responses.size()).isEqualTo(1),
        () -> assertThat(responses.get(0).getFarmSearchResponse().getName()).isEqualTo(
            request.getName())
    );
  }

  public static ExtractableResponse<Response> requestToAddFavorite(AuthResponse response,
      FavoriteRequest request) {
    return given().log().all()
        .header("Authorization",
            response.getTokenType() + " " + response.getAccessToken())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(request)
        .when()
        .post("/api/favorite")
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> requestToFindFavorite(AuthResponse response) {
    return given().log().all()
        .header("Authorization",
            response.getTokenType() + " " + response.getAccessToken())
        .when()
        .get("/api/favorite")
        .then().log().all()
        .extract();
  }

  public static ExtractableResponse<Response> requestToDeleteFavorite(AuthResponse response,
      Long favoriteId) {
    return given().log().all()
        .header("Authorization",
            response.getTokenType() + " " + response.getAccessToken())
        .when()
        .delete(String.format("/api/favorite?favoriteId=%d", favoriteId))
        .then().log().all()
        .extract();
  }
}
