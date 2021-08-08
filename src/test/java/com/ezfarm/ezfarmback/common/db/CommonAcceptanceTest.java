package com.ezfarm.ezfarmback.common.db;

import static io.restassured.RestAssured.given;

import com.ezfarm.ezfarmback.common.database.DatabaseCleanUp;
import com.ezfarm.ezfarmback.user.domain.Role;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.LoginRequest;
import com.ezfarm.ezfarmback.user.dto.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class CommonAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected DatabaseCleanUp databaseCleanUp;

    protected ObjectMapper objectMapper;

    protected User user1;

    protected User user2;

    @BeforeEach
    public void setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port;
        }

        databaseCleanUp.afterPropertiesSet();
        databaseCleanUp.clearUp();

        user1 = createUserOne();
        user2 = createUserTwo();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private User createUserOne() {
        User user = User.builder()
            .name("테스트 유저1")
            .email("test1@email.com")
            .password(passwordEncoder.encode("비밀번호"))
            .role(Role.ROLE_USER)
            .build();
        userRepository.save(user);
        return user;
    }

    private User createUserTwo() {
        User user = User.builder()
            .name("테스트 유저2")
            .email("test2@email.com")
            .password(passwordEncoder.encode("비밀번호"))
            .role(Role.ROLE_USER)
            .build();
        userRepository.save(user);
        return user;
    }

    protected String getAccessJsonWebToken(String secretKey) {
        return Jwts.builder()
            .setSubject(user1.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date())
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

    protected ExtractableResponse<Response> getSignupResponse(SignUpRequest signUpRequest) {
        return given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(signUpRequest)
            .when()
            .post("/api/user/signup")
            .then().log().all()
            .extract();
    }

    protected ExtractableResponse<Response> getLoginResponse(LoginRequest loginRequest)
        throws Exception {
        return given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(objectMapper.writeValueAsString(loginRequest))
            .when()
            .post("/api/user/login")
            .then().log().all()
            .extract();
    }

    protected ExtractableResponse<Response> getAuthenticationResponse(AuthResponse authResponse) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .get("/api/user/access-test")
            .then().log().all()
            .extract();
    }

    protected AuthResponse getAuthResponse(LoginRequest loginRequest) {
        try {
            return getLoginResponse(loginRequest).as(AuthResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
