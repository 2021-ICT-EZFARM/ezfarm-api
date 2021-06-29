package com.ezfarm.ezfarmback.favorite.acceptance;

import com.ezfarm.ezfarmback.common.acceptance.AcceptanceStep;
import com.ezfarm.ezfarmback.common.acceptance.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.farm.acceptance.step.FarmAcceptanceStep;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.favorite.acceptance.step.FavoriteAcceptanceStep;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

public class FavoriteAcceptanceTest extends CommonAcceptanceTest {

    FarmRequest farmRequest;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        farmRequest = new FarmRequest(
            "경기",
            "010-2222-2222",
            "100",
            true,
            FarmType.GLASS,
            CropType.PAPRIKA,
            LocalDate.now()
        );
    }

    @DisplayName("농장 즐겨찾기를 추가한다.")
    @Test
    void addFavorite() throws JsonProcessingException {
        //given
        LoginRequest loginRequest1 = new LoginRequest("test1@email.com", "비밀번호");
        AuthResponse authResponse1 = getAuthResponse(loginRequest1);

        LoginRequest loginRequest2 = new LoginRequest("test2@email.com", "비밀번호");
        AuthResponse authResponse2 = getAuthResponse(loginRequest2);

        //when
        String farmLocation = FarmAcceptanceStep
            .requestToCreateFarmAndGetLocation(authResponse1, farmRequest, objectMapper)
            .header(HttpHeaders.LOCATION);

        String[] split = farmLocation.split("/");
        long farmId = Long.parseLong(split[split.length - 1]);

        ExtractableResponse<Response> response = FavoriteAcceptanceStep
            .requestToAddFavorite(authResponse2, farmId);

        //then
        AcceptanceStep.assertThatStatusIsOk(response);
    }

}
