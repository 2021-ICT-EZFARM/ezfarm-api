package com.ezfarm.ezfarmback.user.acceptance;

import com.ezfarm.ezfarmback.common.acceptance.AcceptanceTest;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("유저 통합 테스트")
public class UserAcceptanceTest extends AcceptanceTest {

    private static SignUpRequest signUpRequest;
    private static LoginRequest loginRequest;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        signUpRequest = new SignUpRequest("highright96", "highright96@gmail.com", "비밀번호");
        loginRequest = new LoginRequest("highright96@gmail.com", "비밀번호");
    }

    @DisplayName("회원가입 요청을 한다.")
    @Test
    void signUpTest() {
        //given
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(signUpRequest)
                .when()
                .post("/api/user/signup")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}

