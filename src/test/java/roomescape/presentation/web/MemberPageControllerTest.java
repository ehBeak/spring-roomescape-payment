package roomescape.presentation.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomescape.presentation.BaseControllerTest;

class MemberPageControllerTest extends BaseControllerTest {

    @ParameterizedTest(name = "{0} 페이지를 조회한다.")
    @ValueSource(strings = {
            "/",
            "/reservation",
            "/login",
            "/signup",
            "/reservation-mine"
    })
    void pageTest(String path) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
