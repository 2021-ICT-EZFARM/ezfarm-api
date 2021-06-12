package com.ezfarm.ezfarmback.security;

import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("시큐리티 테스트(LoginFilter)")
public class LoginFilterTest extends CommonSecurityTest {

    @DisplayName("옳바른 로그인 요청을 한다.")
    @Test
    void ValidLoginTest() throws JsonProcessingException {
        LoginRequest loginRequest = new LoginRequest("test@email.com", "비밀번호");
        ExtractableResponse<Response> response = getLoginResponse(loginRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("잘못된 아이디로 로그인 요청을 한다.")
    @Test
    void UnValidIdLoginTest() throws JsonProcessingException {
        LoginRequest loginRequest = new LoginRequest("test2@email.com", "비밀번호");
        ExtractableResponse<Response> response = getLoginResponse(loginRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("잘못된 비밀번호로 로그인 요청을 한다")
    @Test
    void UnValidPasswordLoginTest() throws JsonProcessingException {
        LoginRequest loginRequest = new LoginRequest("test@email.com", "비밀번호");
        ExtractableResponse<Response> response = getLoginResponse(loginRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
