package com.ezfarm.ezfarmback.security;

import com.ezfarm.ezfarmback.common.acceptance.AcceptanceTest;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import com.ezfarm.ezfarmback.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommonSecurityTest {

    @LocalServerPort
    int port;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    protected ObjectMapper objectMapper;

    protected User mockUser;

    @BeforeEach
    public void setUp() {
        clearUserTable();
        objectMapper = new ObjectMapper();
        mockUser = createMockUser();
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port;
        }
    }

    private void clearUserTable() {
        userRepository.deleteAll();
    }

    protected User createMockUser() {
        User user = User.builder()
                .name("남상우")
                .email("a@gmail.com")
                .password(passwordEncoder.encode("비밀번호"))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
        return user;
    }

    protected String getAccessJsonWebToken(String secretKey) {
        return Jwts.builder()
                .setSubject(mockUser.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    protected ExtractableResponse<Response> getLoginResponse(LoginRequest loginRequest) throws JsonProcessingException {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(loginRequest))
                .when()
                .post("/api/user/login")
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> getAuthenticationResponse(AuthResponse authResponse) {
        ExtractableResponse<Response> response = given().log().all()
                .header("Authorization", authResponse.getTokenType() + " " + authResponse.getAccessToken())
                .when()
                .get("/api/user/access-test")
                .then().log().all()
                .extract();
        return response;
    }

    protected AuthResponse getAuthResponse() throws JsonProcessingException {
        LoginRequest loginRequest = new LoginRequest("a@gmail.com", "비밀번호");
        ExtractableResponse<Response> loginResponse = getLoginResponse(loginRequest);
        return loginResponse.body().as(AuthResponse.class);
    }
}
