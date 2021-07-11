package com.ezfarm.ezfarmback.favorite.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.common.acceptance.AcceptanceStep;
import com.ezfarm.ezfarmback.common.acceptance.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.farm.acceptance.step.FarmAcceptanceStep;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.favorite.acceptance.step.FavoriteAcceptanceStep;
import com.ezfarm.ezfarmback.favorite.dto.FavoriteResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("농가 즐겨찾기 통합 테스트")
public class FavoriteAcceptanceTest extends CommonAcceptanceTest {

    FarmRequest farmRequest;

    LoginRequest loginRequest;

    LoginRequest ownerLoginRequest;

    AuthResponse authResponse;

    AuthResponse ownerAuthResponse;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        farmRequest = FarmRequest.builder()
            .name("테스트 농가 이름1")
            .address("서울")
            .phoneNumber("010-1234-1234")
            .isMain(false)
            .build();

        loginRequest = new LoginRequest("test1@email.com", "비밀번호");
        authResponse = getAuthResponse(loginRequest);

        ownerLoginRequest = new LoginRequest("test2@email.com", "비밀번호");
        ownerAuthResponse = getAuthResponse(ownerLoginRequest);
    }

    @DisplayName("농가 즐겨찾기를 추가한다.")
    @Test
    void addFavorite() throws Exception {
        Long farmId = FarmAcceptanceStep
            .requestToCreateFarmAndGetLocation(authResponse, farmRequest, objectMapper);

        ExtractableResponse<Response> result = FavoriteAcceptanceStep
            .requestToAddFavorite(ownerAuthResponse, farmId);

        AcceptanceStep.assertThatStatusIsOk(result);
    }

    @DisplayName("농가 즐겨찾기를 조회한다.")
    @Test
    void findFavorites() throws Exception {
        Long farmId = FarmAcceptanceStep
            .requestToCreateFarmAndGetLocation(ownerAuthResponse, farmRequest, objectMapper);
        FavoriteAcceptanceStep.requestToAddFavorite(authResponse, farmId);

        ExtractableResponse<Response> response = FavoriteAcceptanceStep
            .requestToFindFavorite(authResponse);
        List<FavoriteResponse> favoriteResponses = response.jsonPath()
            .getList(".", FavoriteResponse.class);

        AcceptanceStep.assertThatStatusIsOk(response);
        FavoriteAcceptanceStep
            .assertThatFindFavorites(favoriteResponses, ownerLoginRequest, farmRequest);
    }

    @DisplayName("농가 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() throws Exception {
        Long farmId = FarmAcceptanceStep
            .requestToCreateFarmAndGetLocation(ownerAuthResponse, farmRequest, objectMapper);
        FavoriteAcceptanceStep.requestToAddFavorite(authResponse, farmId);

        ExtractableResponse<Response> favoriteResponse = FavoriteAcceptanceStep
            .requestToFindFavorite(authResponse);
        List<FavoriteResponse> favoriteResponses = favoriteResponse.jsonPath()
            .getList(".", FavoriteResponse.class);

        ExtractableResponse<Response> deleteResponse = FavoriteAcceptanceStep
            .requestToDeleteFavorite(authResponse, favoriteResponses.get(0).getId());

        List<FavoriteResponse> response = FavoriteAcceptanceStep
            .requestToFindFavorite(authResponse).jsonPath().getList(".", FavoriteResponse.class);

        Assertions.assertAll(
            () -> AcceptanceStep.assertThatStatusIsNoContent(deleteResponse),
            () -> assertThat(response.size()).isEqualTo(0)
        );
    }
}
