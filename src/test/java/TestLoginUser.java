import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;

import static org.apache.http.HttpStatus.SC_OK;

public class TestLoginUser extends BaseTest {
    public TestLoginUser() {
        super();

        this.methodsForTestsUserAPI = new MethodsForTestsUserAPI();
    }

    @Test
    @DisplayName("Login existed user with correct data")
    @Description("Test for POST /api/auth/login endpoint")
    public void loginUserWithCorrectDataReturnsOK() {
        // Создаем объект
        User user = new User("test-email@test.ru", "password", "TestName");

        HashMap<String, String> expectedUserData = new HashMap<>();

        expectedUserData.put("email", user.getEmail());
        expectedUserData.put("name", user.getName());

        // Направляем POST запрос на /api/auth/register для создания пользователя
        methodsForTestsUserAPI.sendPostRequestApiAuthRegister(user);

        // Направляем POST запрос на /api/auth/login для логина созданного пользователя
        Response response = methodsForTestsUserAPI.sendPostRequestApiAuthLogin(new User("test-email@test.ru", "password", null));

        // Сверяем полученный код ответа
        MethodsForCheckResponse.compareStatusCode(response, SC_OK);
        // Сверяем содержимое полей в ответе
        MethodsForCheckResponse.checkBooleanValueOfFieldFromBody(response,"success",true);
        MethodsForCheckResponse.checkHashOfFieldFromBody(response, "user", expectedUserData);

        // Удаляем созданного пользователя
        methodsForTestsUserAPI.sendDeleteRequestApiAuthUser(
            response.body().as(ResponseAuthorizationData.class).getAccessToken()
        );
    }
}
