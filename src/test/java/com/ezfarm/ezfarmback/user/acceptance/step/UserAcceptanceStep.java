package com.ezfarm.ezfarmback.user.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.dto.AuthResponse;
import com.ezfarm.ezfarmback.user.dto.UserResponse;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;

public class UserAcceptanceStep {

    public static void assertThatFindUser(UserResponse userResponse, User user) {
        assertAll(
            () -> assertThat(userResponse.getEmail()).isEqualTo(user.getEmail()),
            () -> assertThat(userResponse.getId()).isNotNull(),
            () -> assertThat(userResponse.getName()).isEqualTo(user.getName())
        );
    }

    public static void assertThatUpdateUser(UserResponse userResponse) {
        assertAll(
            () -> assertThat(userResponse.getAddress()).isEqualTo("수정"),
            () -> assertThat(userResponse.getPhoneNumber()).isEqualTo("010-1111-1111"),
            () -> assertThat(userResponse.getImageUrl()).isNotNull()
        );
    }

    public static ExtractableResponse<Response> requestToReadUser(
        AuthResponse authResponse) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .when()
            .get("/api/user")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> requestToUpdateUser(AuthResponse authResponse)
        throws Exception {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .multiPart(new MultiPartSpecBuilder("010-1111-1111")
                .charset(StandardCharsets.UTF_8)
                .controlName("phoneNumber")
                .build())
            .multiPart(new MultiPartSpecBuilder("수정")
                .charset(StandardCharsets.UTF_8)
                .controlName("address")
                .build())
            .multiPart(new MultiPartSpecBuilder(false)
                .controlName("isDefaultImage")
                .build())
            .multiPart(new MultiPartSpecBuilder(
                new FileInputStream("src/test/resources/images/ezfarm-logo.PNG"))
                .fileName("images.png")
                .controlName("image")
                .mimeType(MediaType.IMAGE_JPEG_VALUE)
                .build())
            .when()
            .patch("/api/user")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> requestToDeleteUser(
        AuthResponse authResponse) {
        return given().log().all()
            .header("Authorization",
                authResponse.getTokenType() + " " + authResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete("/api/user")
            .then().log().all()
            .extract();
    }
}
