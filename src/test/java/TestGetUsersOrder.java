import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class TestGetUsersOrder extends BaseTest {
    public TestGetUsersOrder() {
        super();

        this.methodsForTestsOrdersAPI = new MethodsForTestsOrdersAPI();
        this.methodsForTestsUserAPI = new MethodsForTestsUserAPI();
    }

    @Test
    @DisplayName("Get user orders")
    @Description("Test for GET /api/orders")
    public void getUserOrdersReturnsOK() {
        User user = new User("test-email@test.ru", "password", "TestName");

        // Направляем POST запрос на /api/auth/register для создания пользователя, сохраняем его токен
        String accessToken = methodsForTestsUserAPI
            .sendPostRequestApiAuthRegister(user)
            .body().as(ResponseAuthorizationData.class)
            .getAccessToken();

        Response response = methodsForTestsOrdersAPI.sendGetRequestApiOrder(accessToken);

        MethodsForCheckResponse.compareStatusCode(response, SC_OK);
        MethodsForCheckResponse.checkBooleanValueOfFieldFromBody(response, "success", true);


        methodsForTestsUserAPI.sendDeleteRequestApiAuthUser(accessToken);
    }

    @Test
    @DisplayName("Get user orders via unauthorized")
    @Description("Test for GET /api/orders")
    public void getUserOrderViaUnauthorizedReturns401() {
        Response response = methodsForTestsOrdersAPI.sendGetRequestApiOrder();

        MethodsForCheckResponse.compareStatusCode(response, SC_UNAUTHORIZED);
        MethodsForCheckResponse.checkBooleanValueOfFieldFromBody(response, "success", false);
        MethodsForCheckResponse.checkValueOfFieldFromBody(response, "message", "You should be authorised");
    }
}
