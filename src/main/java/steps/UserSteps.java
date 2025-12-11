package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.User;
import static io.restassured.RestAssured.given;

public class UserSteps {
    public static final String REGISTER = "/api/auth/register";
    public static final String LOGIN = "/api/auth/login";
    public static final String USER = "/api/auth/user";


    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        ValidatableResponse response = given()
                .body(user)
                .when()
                .post(REGISTER)
                .then();

        try {
            String accessToken = response.extract().body().path("accessToken");
            user.setAccessToken(accessToken.substring(7));
            String refreshToken = response.extract().body().path("refreshToken");
            user.setRefreshToken(refreshToken);
        } catch (Exception e) {
        }

        return response;
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(User user) {
        ValidatableResponse response = given()
                .body(user)
                .when()
                .post(LOGIN)
                .then();

        try {
            String accessToken = response.extract().body().path("accessToken");
            user.setAccessToken(accessToken.substring(7));
            String refreshToken = response.extract().body().path("refreshToken");
            user.setRefreshToken(refreshToken);
        } catch (Exception e) {
        }

        return response;
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(User user) {
        return given()
                .auth().oauth2(user.getRefreshToken())
                .delete(USER)
                .then();
    }
}
