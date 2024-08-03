import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.HashMap;

import static org.apache.http.HttpStatus.*;

public class TestCreateUser extends BaseTest {
    public TestCreateUser() {
        super();

        this.methodsForTestsUserAPI = new MethodsForTestsUserAPI();
    }

    @Test
    @DisplayName("Create user with full set of data")
    @Description("Test for POST /api/auth/register endpoint")
    public void createUserWithFullDataReturnsOK() {
        // Создаем объект
        User user = new User("test-email@test.ru", "password", "TestName");

        HashMap<String, String> expectedUserData = new HashMap<>();

        expectedUserData.put("email", user.getEmail());
        expectedUserData.put("name", user.getName());

        // Направляем POST запрос на /api/auth/register для создания пользователя
        Response response = methodsForTestsUserAPI.sendPostRequestApiAuthRegister(user);

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

    @Test
    @DisplayName("Create already existed user with a full set of data")
    @Description("Test for POST /api/auth/register endpoint")
    public void createRegisteredUserReturns403() {
        // Создаем объект
        User user = new User("test-email@test.ru","password", "TestName");

        // Направляем POST запрос на /api/auth/register для создания пользователя
        Response firstResponse = methodsForTestsUserAPI.sendPostRequestApiAuthRegister(user);

        // Направляем ПОВТОРНЫЙ POST запрос на /api/auth/register для создания уже существующего пользователя
        Response secondResponse = methodsForTestsUserAPI.sendPostRequestApiAuthRegister(user);

        // Сверяем полученный код ответа
        MethodsForCheckResponse.compareStatusCode(secondResponse, SC_FORBIDDEN);
        // Сверяем содержимое полей в ответе
        MethodsForCheckResponse.checkBooleanValueOfFieldFromBody(secondResponse,"success",false);
        MethodsForCheckResponse.checkValueOfFieldFromBody(secondResponse, "message", "User already exists");

        // Удаляем созданного пользователя
        methodsForTestsUserAPI.sendDeleteRequestApiAuthUser(
            firstResponse.body().as(ResponseAuthorizationData.class).getAccessToken()
        );
    }

    @Test
    @DisplayName("Create user without name")
    @Description("Test for POST /api/auth/register endpoint")
    public void createUserWithoutNameReturns403() {
        // Создаем объект
        User user = new User("test-email@test.ru", "password", null);

        // Направляем POST запрос на /api/auth/register для создания пользователя
        Response response = methodsForTestsUserAPI.sendPostRequestApiAuthRegister(user);

        // Сверяем полученный код ответа
        MethodsForCheckResponse.compareStatusCode(response, SC_FORBIDDEN);
        // Сверяем содержимое полей в ответе
        MethodsForCheckResponse.checkBooleanValueOfFieldFromBody(response,"success",false);
        MethodsForCheckResponse.checkValueOfFieldFromBody(response, "message", "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Create user without password")
    @Description("Test for POST /api/auth/register endpoint")
    public void createUserWithoutPasswordReturns403() {
        // Создаем объект
        User user = new User("test-email@test.ru",null, "TestName");

        // Направляем POST запрос на /api/auth/register для создания пользователя
        Response response = methodsForTestsUserAPI.sendPostRequestApiAuthRegister(user);

        // Сверяем полученный код ответа
        MethodsForCheckResponse.compareStatusCode(response, SC_FORBIDDEN);
        // Сверяем содержимое полей в ответе
        MethodsForCheckResponse.checkBooleanValueOfFieldFromBody(response,"success",false);
        MethodsForCheckResponse.checkValueOfFieldFromBody(response, "message", "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Create user without email")
    @Description("Test for POST /api/auth/register endpoint")
    public void createUserWithoutEmailReturns403() {
        // Создаем объект
        User user = new User(null,"password", "TestName");

        // Направляем POST запрос на /api/auth/register для создания пользователя
        Response response = methodsForTestsUserAPI.sendPostRequestApiAuthRegister(user);

        // Сверяем полученный код ответа
        MethodsForCheckResponse.compareStatusCode(response, SC_FORBIDDEN);
        // Сверяем содержимое полей в ответе
        MethodsForCheckResponse.checkBooleanValueOfFieldFromBody(response,"success",false);
        MethodsForCheckResponse.checkValueOfFieldFromBody(response, "message", "Email, password and name are required fields");
    }
}
