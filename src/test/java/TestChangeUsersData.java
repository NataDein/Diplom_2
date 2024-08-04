import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

import static org.apache.http.HttpStatus.*;

public class TestChangeUsersData extends BaseTest {
    public TestChangeUsersData() {
        super();

        this.methodsForTestsUserAPI = new MethodsForTestsUserAPI();
    }

    @Test
    @DisplayName("Change user data with authorization")
    @Description("Test for PATCH /api/auth/user")
    public void changeUserDataViaAuthorizedUserReturnsOK() {
        User user = new User("test-email@test.ru", "password", "TestName");
        User changedUser = new User("changed-test-email@test.ru", "changed-password", "changed-TestName");

        HashMap<String, String> expectedUserData = new HashMap<>();

        expectedUserData.put("email", changedUser.getEmail());
        expectedUserData.put("name", changedUser.getName());

        // Направляем POST запрос на /api/auth/register для создания пользователя, сохраняем его токен
        String userAccessToken = methodsForTestsUserAPI
            .sendPostRequestApiAuthRegister(user)
            .body().as(ResponseAuthorizationData.class)
            .getAccessToken();

        Response response = methodsForTestsUserAPI.sendPatchRequestApiAuthUser(
            changedUser,
            userAccessToken
        );

        // Сверяем полученный код ответа
        MethodsForCheckResponse.compareStatusCode(response, SC_OK);
        // Сверяем содержимое полей в ответе
        MethodsForCheckResponse.checkBooleanValueOfFieldFromBody(response,"success",true);
        MethodsForCheckResponse.checkHashOfFieldFromBody(response, "user", expectedUserData);

        // Удаляем созданного пользователя
        methodsForTestsUserAPI.sendDeleteRequestApiAuthUser(userAccessToken);
    }

    @Test
    @DisplayName("Change user data with authorization and email in use")
    @Description("Test for PATCH /api/auth/user")
    public void changeUserDataViaAuthorizedUserReturns403() {
        User user = new User("test-email@test.ru", "password", "TestName");
        User existedUser = new User("existed-test-email@test.ru", "existed-password", "ExistedTestName");
        User changedUser = new User("existed-test-email@test.ru", "changed-password", "changed-TestName");

        HashMap<String, String> expectedUserData = new HashMap<>();

        expectedUserData.put("email", changedUser.getEmail());
        expectedUserData.put("name", changedUser.getName());

        // Направляем POST запрос на /api/auth/register для создания пользователя, сохраняем его токен
        String existedUserAccessToken = methodsForTestsUserAPI
            .sendPostRequestApiAuthRegister(existedUser)
            .body().as(ResponseAuthorizationData.class)
            .getAccessToken();

        // Направляем POST запрос на /api/auth/register для создания пользователя, сохраняем его токен
        String userAccessToken = methodsForTestsUserAPI
            .sendPostRequestApiAuthRegister(user)
            .body().as(ResponseAuthorizationData.class)
            .getAccessToken();

        Response response = methodsForTestsUserAPI.sendPatchRequestApiAuthUser(
            changedUser,
            userAccessToken
        );

        // Сверяем полученный код ответа
        MethodsForCheckResponse.compareStatusCode(response, SC_FORBIDDEN);
        // Сверяем содержимое полей в ответе
        MethodsForCheckResponse.checkBooleanValueOfFieldFromBody(response,"success",false);
        MethodsForCheckResponse.checkValueOfFieldFromBody(response, "message", "User with such email already exists");

        // Удаляем созданного пользователя
        methodsForTestsUserAPI.sendDeleteRequestApiAuthUser(userAccessToken);
        methodsForTestsUserAPI.sendDeleteRequestApiAuthUser(existedUserAccessToken);
    }

    @Test
    @DisplayName("Change user data without authorization")
    @Description("Test for PATCH /api/auth/user")
    public void changeUserDataViaUnauthorizedUserReturns401() {
        User changedUser = new User("changed-test-email@test.ru", "changed-password", "changed-TestName");

        Response response = methodsForTestsUserAPI.sendPatchRequestApiAuthUser(changedUser);

        // Сверяем полученный код ответа
        MethodsForCheckResponse.compareStatusCode(response, SC_UNAUTHORIZED);
        // Сверяем содержимое полей в ответе
        MethodsForCheckResponse.checkBooleanValueOfFieldFromBody(response,"success",false);
        MethodsForCheckResponse.checkValueOfFieldFromBody(response, "message", "You should be authorised");
    }
}
