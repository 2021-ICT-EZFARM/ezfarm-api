package com.ezfarm.ezfarmback.security;

import com.ezfarm.ezfarmback.config.security.AppProperties;
import com.ezfarm.ezfarmback.security.local.TokenProvider;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("시큐리티 테스트(Token)")
public class TokenTest extends CommonSecurityTest {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    AppProperties appProperties;

    @DisplayName("옳바른 토큰을 갖는다.")
    @Test
    void ValidToken() throws JsonProcessingException {
        //given
        LoginRequest loginRequest = new LoginRequest("test@email.com", "비밀번호");
        AuthResponse authResponse = getAuthResponse(loginRequest);
        String accessToken = authResponse.getAccessToken();

        //when
        boolean isValidToken = tokenProvider.validateToken(accessToken);
        String emailFromToken = tokenProvider.getUserEmailFromToken(accessToken);

        //then
        assertThat(isValidToken).isTrue();
        assertThat(emailFromToken).isEqualTo(mockUser.getEmail());
    }

    @DisplayName("기간이 만료된 토큰은 ExpiredJwtException 에러를 발생시킨다.")
    @Test
    void UnValidTokenExpiredJwtException() {
        //given
        String accessToken = getAccessJsonWebToken(appProperties.getAuth().getTokenSecret());

        //when then
        assertThat(tokenProvider.validateToken(accessToken)).isFalse();
        assertThatThrownBy(() -> tokenProvider.getUserEmailFromToken(accessToken))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @DisplayName("잘못된 키로 인증되는 토큰은 SignatureException 에러를 발생시킨다.")
    @Test
    void UnValidTokenSignatureException() {
        //given
        String accessToken = getAccessJsonWebToken("secretKey");

        //when then
        assertThat(tokenProvider.validateToken(accessToken)).isFalse();
        assertThatThrownBy(() -> tokenProvider.getUserEmailFromToken(accessToken))
                .isInstanceOf(SignatureException.class);
    }
}
