package roomescape.presentation.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.exception.ErrorResponse;
import roomescape.fixture.Fixture;
import roomescape.presentation.BaseControllerTest;

class AdminPageControllerTest extends BaseControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @ParameterizedTest(name = "{0} 페이지를 조회한다.")
    @ValueSource(strings = {
            "/admin",
            "/admin/time",
            "/admin/theme",
            "/admin/reservation",
            "/admin/waiting",
    })
    @DisplayName("어드민 페이지를 조회한다.")
    void pageTest(String path) {
        Member admin = memberRepository.save(Fixture.MEMBER_ADMIN);
        String token = tokenProvider.createToken(admin.getId().toString());

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie("token", token)
                .when().get(path)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("로그인하지 않으면 401 에러가 발생한다.")
    void notLoggedIn() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/admin")
                .then().log().all()
                .extract();

        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            softly.assertThat(errorResponse.message()).isEqualTo("로그인이 필요합니다.");
        });
    }

    @Test
    @DisplayName("로그인을 했지만 어드민이 아니면 403 에러가 발생한다.")
    void notAdmin() {
        Member user = memberRepository.save(Fixture.MEMBER_USER);
        String token = tokenProvider.createToken(user.getId().toString());

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .extract();

        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
            softly.assertThat(errorResponse.message()).isEqualTo("어드민 권한이 필요합니다.");
        });
    }
}
