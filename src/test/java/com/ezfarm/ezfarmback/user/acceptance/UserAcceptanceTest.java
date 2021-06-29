package com.ezfarm.ezfarmback.user.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.ezfarm.ezfarmback.common.acceptance.AcceptanceStep;
import com.ezfarm.ezfarmback.common.acceptance.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.farm.dto.FarmRequest;
import com.ezfarm.ezfarmback.user.acceptance.step.UserAcceptanceStep;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.ezfarm.ezfarmback.user.dto.UserResponse;
import com.ezfarm.ezfarmback.user.dto.UserUpdateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
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
            "010-2222-2222",
            "100",
            true,
            FarmType.GLASS,
            CropType.PAPRIKA,
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
        //given
        LoginRequest loginRequest = new LoginRequest("test1@email.com", "비밀번호");
        AuthResponse authResponse = getAuthResponse(loginRequest);

        //when
        ExtractableResponse<Response> response = UserAcceptanceStep.requestToReadUser(
            authResponse);

        UserResponse userResponse = response.as(UserResponse.class);

        //then
        AcceptanceStep.assertThatStatusIsOk(response);
        assertAll(
            () -> assertThat(userResponse.getEmail()).isEqualTo(user1.getEmail()),
            () -> assertThat(userResponse.getId()).isNotNull(),
            () -> assertThat(userResponse.getRole()).isEqualTo(user1.getRole()),
            () -> assertThat(userResponse.getName()).isEqualTo(user1.getName())
        );
    }

    @DisplayName("유저 정보를 수정한다.")
    @Test
    void updateUser() throws JsonProcessingException {
        //given
        LoginRequest loginRequest = new LoginRequest("test1@email.com", "비밀번호");
        AuthResponse authResponse = getAuthResponse(loginRequest);

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("update", "010-1234-1234",
            "address", "image");

        //when
        ExtractableResponse<Response> response = UserAcceptanceStep.requestToUpdateUser(
            authResponse, userUpdateRequest, objectMapper);

        //then
        AcceptanceStep.assertThatStatusIsOk(response);
    }

    @DisplayName("회원 탈퇴를 한다.")
    @Test
    void deleteUser() {
        //given
        LoginRequest loginRequest = new LoginRequest("test1@email.com", "비밀번호");
        AuthResponse authResponse = getAuthResponse(loginRequest);

        //when
        ExtractableResponse<Response> response = UserAcceptanceStep.requestToDeleteUser(
            authResponse);

        //then
        AcceptanceStep.assertThatStatusIsNoContent(response);
    }
}

