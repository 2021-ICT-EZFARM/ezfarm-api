package com.ezfarm.ezfarmback.user.acceptance;

import com.ezfarm.ezfarmback.common.acceptance.AcceptanceStep;
import com.ezfarm.ezfarmback.common.acceptance.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.user.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("유저 통합 테스트")
public class UserAcceptanceTest extends CommonAcceptanceTest {

    @DisplayName("회원가입을 한다.")
    @Test
    void signUpTest() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("테스트 유저", "newTest@email.com", "비밀번호");
        ExtractableResponse<Response> response = getSignupResponse(signUpRequest);

        //then
        AcceptanceStep.assertThatStatusIsCreated(response);
    }

    @DisplayName("유저 정보를 조회한다.")
    @Test
    void readUser() {
        //given
        LoginRequest loginRequest = new LoginRequest("test@email.com", "비밀번호");
        AuthResponse authResponse = getAuthResponse(loginRequest);

        //when
        ExtractableResponse<Response> response = given().log().all()
                .header("Authorization", authResponse.getTokenType() + " " + authResponse.getAccessToken())
                .when()
                .get("/api/user")
                .then().log().all()
                .extract();

        UserResponse userResponse = response.as(UserResponse.class);
        ;
        //then
        AcceptanceStep.assertThatStatusIsOk(response);
        assertAll(
                () -> assertThat(userResponse.getEmail()).isEqualTo(mockUser.getEmail()),
                () -> assertThat(userResponse.getId()).isNotNull(),
                () -> assertThat(userResponse.getRole()).isEqualTo(mockUser.getRole()),
                () -> assertThat(userResponse.getName()).isEqualTo(mockUser.getName())
        );
    }

    @DisplayName("유저 정보를 수정한다.")
    @Test
    void updateUser() throws JsonProcessingException {
        //given
        LoginRequest loginRequest = new LoginRequest("test@email.com", "비밀번호");
        AuthResponse authResponse = getAuthResponse(loginRequest);

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("update", "010-1234-1234", "address", "image");

        //when
        ExtractableResponse<Response> response = given().log().all()
                .header("Authorization", authResponse.getTokenType() + " " + authResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(userUpdateRequest))
                .when()
                .patch("/api/user")
                .then().log().all()
                .extract();

        //then
        AcceptanceStep.assertThatStatusIsOk(response);
    }

    @DisplayName("회원 탈퇴를 한다.")
    @Test
    void deleteUser() {
        //given
        LoginRequest loginRequest = new LoginRequest("test@email.com", "비밀번호");
        AuthResponse authResponse = getAuthResponse(loginRequest);

        //when
        ExtractableResponse<Response> response = given().log().all()
                .header("Authorization", authResponse.getTokenType() + " " + authResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/user")
                .then().log().all()
                .extract();

        //then
        AcceptanceStep.assertThatStatusIsNoContent(response);
    }
}

