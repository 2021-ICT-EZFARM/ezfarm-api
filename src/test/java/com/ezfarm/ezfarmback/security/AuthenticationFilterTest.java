package com.ezfarm.ezfarmback.security;


import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("시큐리티 테스트(AuthenticationFilter)")
public class AuthenticationFilterTest extends CommonSecurityTest {

    @DisplayName("옳바른 토큰을 가지고 API에 요청한다.")
    @Test
    void AccessApiWithValidToken() throws JsonProcessingException {
        //given
        AuthResponse authResponse = getAuthResponse();

        //when
        ExtractableResponse<Response> response = getAuthenticationResponse(authResponse);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("옳바른 현재 유저 정보를 가지고 있다.")
    @Test
    void HaveValidCurrentUserInfo() throws JsonProcessingException {
        //given
        AuthResponse authResponse = getAuthResponse();

        //when
        ExtractableResponse<Response> response = getAuthenticationResponse(authResponse);
        UserResponse userResponse = response.body().as(UserResponse.class);

        //then
        assertThat(userResponse).isEqualTo(UserResponse.of(mockUser));

    }
}
