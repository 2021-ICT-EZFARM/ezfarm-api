package com.ezfarm.ezfarmback.favorite.acceptance;

import com.ezfarm.ezfarmback.common.acceptance.AcceptanceStep;
import com.ezfarm.ezfarmback.common.acceptance.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.farm.acceptance.step.FarmAcceptanceStep;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.favorite.acceptance.step.FavoriteAcceptanceStep;
import com.ezfarm.ezfarmback.favorite.dto.FavoriteResponse;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("농장 즐겨찾기 통합 테스트")
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
        farmRequest = new FarmRequest(
            "경기",
            "테스트 이름",
            "010-2222-2222",
            "100",
            true,
            FarmType.GLASS,
            CropType.PAPRIKA,
            LocalDate.now()
        );

        loginRequest = new LoginRequest("test1@email.com", "비밀번호");
        authResponse = getAuthResponse(loginRequest);

        ownerLoginRequest = new LoginRequest("test2@email.com", "비밀번호");
        ownerAuthResponse = getAuthResponse(ownerLoginRequest);
    }

    @DisplayName("농장 즐겨찾기를 추가한다.")
    @Test
    void addFavorite() throws JsonProcessingException {
        //when
        ExtractableResponse<Response> response = FarmAcceptanceStep
            .requestToCreateFarm(authResponse, farmRequest, objectMapper);

        Long farmLocation = FarmAcceptanceStep.getLocation(response);

        ExtractableResponse<Response> result = FavoriteAcceptanceStep
            .requestToAddFavorite(ownerAuthResponse, farmLocation);

        //then
        AcceptanceStep.assertThatStatusIsOk(result);
    }

    @DisplayName("농장 즐겨찾기를 조회한다.")
    @Test
    void findFavorites() throws JsonProcessingException {
        //given
        ExtractableResponse<Response> farmResponse = FarmAcceptanceStep
            .requestToCreateFarm(ownerAuthResponse, farmRequest, objectMapper);
        FavoriteAcceptanceStep
            .requestToAddFavorite(authResponse, FarmAcceptanceStep.getLocation(farmResponse));

        //when
        ExtractableResponse<Response> response = FavoriteAcceptanceStep
            .requestToFindFavorite(authResponse);
        List<FavoriteResponse> favoriteResponses = response.jsonPath()
            .getList(".", FavoriteResponse.class);

        //then
        AcceptanceStep.assertThatStatusIsOk(response);
        FavoriteAcceptanceStep
            .assertThatFindFavorites(favoriteResponses, ownerLoginRequest, farmRequest);
    }

    @DisplayName("농장 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() throws JsonProcessingException {
        //given
        ExtractableResponse<Response> farmResponse = FarmAcceptanceStep
            .requestToCreateFarm(ownerAuthResponse, farmRequest, objectMapper);

        FavoriteAcceptanceStep
            .requestToAddFavorite(authResponse, FarmAcceptanceStep.getLocation(farmResponse));

        ExtractableResponse<Response> favoriteResponse = FavoriteAcceptanceStep
            .requestToFindFavorite(authResponse);
        List<FavoriteResponse> favoriteResponses = favoriteResponse.jsonPath()
            .getList(".", FavoriteResponse.class);

        //when
        ExtractableResponse<Response> response = FavoriteAcceptanceStep
            .requestToDeleteFavorite(authResponse, favoriteResponses.get(0).getId());

        //then
        FavoriteAcceptanceStep.assertThatDeleteFavorite(authResponse, response);
    }
}
