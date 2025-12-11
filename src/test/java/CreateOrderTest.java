import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import model.Order;
import model.User;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;


@Epic("Функционал заказа")
@Feature("Создание заказа")
public class CreateOrderTest extends BaseTest{

    private UserSteps userSteps = new UserSteps();
    private User user;
    Faker faker = new Faker();

    private OrderSteps orderSteps= new OrderSteps();
    private Order order;

    private final static List<String> INGREDIENTS = List.of("61c0c5a71d1f82001bdaaa6d",
            "61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa6e");

    @Before
    public void setUp() {
        user = new User();
        user
                .setEmail(faker.internet().emailAddress())
                .setPassword(faker.regexify("[a-z0-9]{6}"))
                .setName(faker.name().firstName());

        userSteps.createUser(user);

        order = new Order();
        order
                .setIngredients(INGREDIENTS);
    }

    @Test
    @DisplayName("Создание заказа: с авторизацией, добавлены существующие ингредиенты")
    public void createOrderWithAuthorizationWithIngredients() {
        orderSteps.createOrder(user, order)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа: с авторизацией, добавлен несуществующий ингредиент")
    public void createOrderWithAuthorizationWithUnexistentIngredient() {
        order.setIngredients(List.of(faker.regexify("[a-z0-9]{24}")));
        orderSteps.createOrder(user, order)
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа: с авторизацией, без ингредиентов")
    public void createOrderWithAuthorizationNoIngredients() {
        order.setIngredients(null);
        orderSteps.createOrder(user, order)
                .statusCode(400)
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа: без авторизации")
    public void createOrderWithoutAuthorization() {
        orderSteps.createOrderWithoutAuthorization(order)
                .body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        try {
            userSteps.deleteUser(user);
        } catch (Exception e) {
        }
    }
}
