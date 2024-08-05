import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

@RunWith(Parameterized.class)
public class TestLoginUserWithIncorrectData extends BaseTest {
    private final String EMAIL;
    private final String PASSWORD;
    private final int HTTP_STATUS_CODE;

    public TestLoginUserWithIncorrectData(String email, String password, int httpStatusCode) {
        super();

        this.methodsForTestsUserAPI = new MethodsForTestsUserAPI();

        this.EMAIL = email;
        this.PASSWORD = password;
        this.HTTP_STATUS_CODE = httpStatusCode;
    }

    @Parameterized.Parameters(name = "{index}: email - {0}, password - {1}")
    public static Object[][] getLoginData() {
        return new Object[][] {
            {null, "password", SC_UNAUTHORIZED},
            {"test-email@test.ru", null, SC_UNAUTHORIZED},
            {null, null, SC_UNAUTHORIZED},
            {"incorrect-test-email@test.ru", "password", SC_UNAUTHORIZED},
            {"test-email@test.ru", "incorrect-password", SC_UNAUTHORIZED},
            {"incorrect-test-email@test.ru", "incorrect-password", SC_UNAUTHORIZED},
        };
    }


    @Test
    @DisplayName("Login existed user with incorrect data")
    @Description("Test for POST /api/auth/login endpoint")
    public void loginUserWithCorrectDataReturns401() {
        // Создаем объект
        User user = new User("test-email@test.ru", "password", "TestName");

        // Направляем POST запрос на /api/auth/register для создания пользователя
        Response creationResponse = methodsForTestsUserAPI.sendPostRequestApiAuthRegister(user);

        // Направляем POST запрос на /api/auth/login для логина созданного пользователя
        Response response = methodsForTestsUserAPI.sendPostRequestApiAuthLogin(
            new User(EMAIL, PASSWORD, null)
        );

        // Сверяем полученный код ответа
        MethodsForCheckResponse.compareStatusCode(response, HTTP_STATUS_CODE);
        // Сверяем содержимое полей в ответе
        MethodsForCheckResponse.checkBooleanValueOfFieldFromBody(response,"success",false);
        MethodsForCheckResponse.checkValueOfFieldFromBody(response, "message", "email or password are incorrect");

        // Удаляем созданного пользователя
        methodsForTestsUserAPI.sendDeleteRequestApiAuthUser(
            methodsForTestsUserAPI.getAccessToken(creationResponse)
        );
    }
}
