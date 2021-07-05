package com.ezfarm.ezfarmback.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.ezfarm.ezfarmback.common.acceptance.CommonAcceptanceTest;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("시큐리티 테스트(LoginFilter)")
public class LoginFilterTest extends CommonAcceptanceTest {

    @DisplayName("옳바른 로그인 요청을 한다.")
    @Test
    void ValidLoginTest() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test1@email.com", "비밀번호");
        ExtractableResponse<Response> response = getLoginResponse(loginRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("잘못된 아이디로 로그인 요청을 한다.")
    @Test
    void inValidIdLoginTest() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test3@email.com", "비밀번호");
        ExtractableResponse<Response> response = getLoginResponse(loginRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("잘못된 비밀번호로 로그인 요청을 한다")
    @Test
    void inValidPasswordLoginTest() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test1@email.com", "잘못된 비밀번호");
        ExtractableResponse<Response> response = getLoginResponse(loginRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
