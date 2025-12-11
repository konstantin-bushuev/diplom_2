package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.User;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    public static final String ORDERS = "/api/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(User user, Order order) {
        return given()
                .auth().oauth2(user.getAccessToken())
                .body(order)
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuthorization(Order order) {
        return given()
                .body(order)
                .when()
                .post(ORDERS)
                .then();
    }
}
