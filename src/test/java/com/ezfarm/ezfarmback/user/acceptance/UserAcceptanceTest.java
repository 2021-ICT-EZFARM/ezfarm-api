package com.ezfarm.ezfarmback.user.acceptance;

import com.ezfarm.ezfarmback.common.db.AcceptanceStep;
import com.ezfarm.ezfarmback.common.db.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.user.acceptance.step.UserAcceptanceStep;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.dto.UserResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("유저 통합 테스트")
public class UserAcceptanceTest extends CommonAcceptanceTest {

    FarmRequest farmRequest;

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
            FarmType.GLASS.toString(),
            CropType.PAPRIKA.toString(),
            LocalDate.now()
        );
    }

    @DisplayName("회원가입을 한다.")
    @Test
    void signUpTest() {
        SignUpRequest signUpRequest = new SignUpRequest("테스트 유저", "newtest1@email.com", "비밀번호");
        ExtractableResponse<Response> response = getSignupResponse(signUpRequest);

        AcceptanceStep.assertThatStatusIsCreated(response);
    }

    @DisplayName("유저 정보를 조회한다.")
    @Test
    void readUser() {
        LoginRequest loginRequest = new LoginRequest("test1@email.com", "비밀번호");
        AuthResponse authResponse = getAuthResponse(loginRequest);

        ExtractableResponse<Response> response = UserAcceptanceStep.requestToReadUser(
            authResponse);
        UserResponse userResponse = response.jsonPath()
            .getObject(".", UserResponse.class);

        AcceptanceStep.assertThatStatusIsOk(response);
        UserAcceptanceStep.assertThatFindUser(userResponse, user1);
    }

    @DisplayName("유저 정보를 수정한다.")
    @Test
    void updateUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test1@email.com", "비밀번호");
        AuthResponse authResponse = getAuthResponse(loginRequest);

        ExtractableResponse<Response> updateResponse = UserAcceptanceStep
            .requestToUpdateUser(authResponse);

        UserResponse findUserResponse = UserAcceptanceStep.requestToReadUser(
            authResponse).jsonPath().getObject(".", UserResponse.class);

        AcceptanceStep.assertThatStatusIsOk(updateResponse);
        UserAcceptanceStep.assertThatUpdateUser(findUserResponse);
    }

    @DisplayName("회원 탈퇴를 한다.")
    @Test
    void deleteUser() {
        LoginRequest loginRequest = new LoginRequest("test1@email.com", "비밀번호");
        AuthResponse authResponse = getAuthResponse(loginRequest);

        ExtractableResponse<Response> response = UserAcceptanceStep.requestToDeleteUser(
            authResponse);

        AcceptanceStep.assertThatStatusIsNoContent(response);
    }
}

