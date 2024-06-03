package roomescape.presentation.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomescape.application.dto.request.LoginRequest;
import roomescape.domain.member.MemberRepository;
import roomescape.fixture.Fixture;
import roomescape.presentation.BaseControllerTest;

class AuthControllerTest extends BaseControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    private String token;

    @TestFactory
    @DisplayName("로그인, 로그인 상태 확인, 로그아웃을 한다.")
    Stream<DynamicTest> authControllerTests() {
        memberRepository.save(Fixture.MEMBER_USER);

        return Stream.of(
                DynamicTest.dynamicTest("로그인한다.", this::login),
                DynamicTest.dynamicTest("로그인 상태를 확인한다.", this::checkLogin),
                DynamicTest.dynamicTest("로그아웃한다.", this::logout)
        );
    }

    @Test
    @DisplayName("로그인하지 않으면 로그인 상태를 확인할 수 없다.")
    void checkLoginFailWhenNotLoggedIn() {
        String token = "invalid token";

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    void login() {
        LoginRequest request = new LoginRequest("user@gmail.com", "abc123");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .extract();

        token = response.cookie("token");

        assertThat(token).isNotNull();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    void checkLogin() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    void logout() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie("token", token)
                .when().post("/logout")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
