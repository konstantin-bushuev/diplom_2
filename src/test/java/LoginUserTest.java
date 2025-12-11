import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;
import static org.hamcrest.CoreMatchers.equalTo;

@Epic("Функционал пользователя")
@Feature("Логин пользователя")
public class LoginUserTest extends BaseTest {

    private UserSteps userSteps = new UserSteps();
    private User user;
    Faker faker = new Faker();

    @Before
    public void setUp() {
        user = new User();
        user
                .setEmail(faker.internet().emailAddress())
                .setPassword(faker.regexify("[a-z0-9]{6}"))
                .setName(faker.name().firstName());

        userSteps.createUser(user);
    }

    @Test
    @DisplayName("Логин пользователя: переданы email, пароль")
    public void loginUserAllDataProvided() {
        userSteps
                .loginUser(user)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Логин пользователя: передан неверный email")
    public void loginUserUnexistentEmailProvided() {
        user.setEmail(faker.internet().emailAddress());
        userSteps
                .loginUser(user)
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин пользователя: передан неверный пароль")
    public void loginUserUnexistentPasswordProvided() {
        user.setPassword(faker.regexify("[a-z0-9]{6}"));
        userSteps
                .loginUser(user)
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин пользователя: не передан email")
    public void loginUserNoEmailProvided() {
        user.setEmail(null);
        userSteps
                .loginUser(user)
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин пользователя: не передан пароль")
    public void loginUserNoPasswordProvided() {
        user.setPassword(null);
        userSteps
                .loginUser(user)
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        try {
            userSteps.deleteUser(user);
        } catch (Exception e) {
        }
    }
}
