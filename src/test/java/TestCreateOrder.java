import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class TestCreateOrder extends BaseTest {
    private User user;
    private String accessToken;

    public TestCreateOrder(User user) {
        super();
        this.user = user;

        this.methodsForTestsOrdersAPI = new MethodsForTestsOrdersAPI();
        this.methodsForTestsUserAPI = new MethodsForTestsUserAPI();
    }

    @Parameterized.Parameters(name = "{index}: user: {0}")
    public static Object[][] getAccessToken() {
        User user = new User("test-email@test.ru", "password", "TestName");

        return new Object[][] {
            { null },
            { user },
        };
    }

    @Before
    public void createUser() {
        if (user == null) return;

        // Направляем POST запрос на /api/auth/register для создания пользователя, сохраняем его токен
        this.accessToken = methodsForTestsUserAPI
            .sendPostRequestApiAuthRegister(user)
            .body().as(ResponseAuthorizationData.class)
            .getAccessToken();
    }

    @After
    public void deleteUser() {
        if (user == null) return;

        methodsForTestsUserAPI.sendDeleteRequestApiAuthUser(this.accessToken);
    }

    @Test
    @DisplayName("Create order")
    @Description("Test for POST /api/orders")
    public void createOrderWithCorrectDataReturnsOK() {
        Ingredient[] responseIngredients = methodsForTestsOrdersAPI
            .sendGetRequestApiIngredients()
            .body().as(ResponseIngredientsData.class)
            .getData();

        RequestCreateOrderDetails orderDetails = new RequestCreateOrderDetails(
            new String[] { responseIngredients[0].get_id() }
        );

        Response response = accessToken != null
            ? methodsForTestsOrdersAPI.sendPostRequestApiOrder(orderDetails, accessToken)
            : methodsForTestsOrdersAPI.sendPostRequestApiOrder(orderDetails);

        MethodsForCheckResponse.compareStatusCode(response, SC_OK);
        MethodsForCheckResponse.checkBooleanValueOfFieldFromBody(response, "success", true);
    }

    @Test
    @DisplayName("Create order with incorrect ingredients")
    @Description("Test for POST /api/orders")
    public void createOrderWithIncorrectDataReturns500() {
        RequestCreateOrderDetails orderDetails = new RequestCreateOrderDetails(
            new String[] { "incorrect-ingredient-id" }
        );

        Response response = methodsForTestsOrdersAPI.sendPostRequestApiOrder(orderDetails);

        MethodsForCheckResponse.compareStatusCode(response, SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Create order with null ingredient")
    @Description("Test for POST /api/orders")
    public void createOrderWithBrokenDataReturns400() {
        RequestCreateOrderDetails orderDetails = new RequestCreateOrderDetails(
            new String[] { null }
        );

        Response response = methodsForTestsOrdersAPI.sendPostRequestApiOrder(orderDetails);

        MethodsForCheckResponse.compareStatusCode(response, SC_BAD_REQUEST);
        MethodsForCheckResponse.checkBooleanValueOfFieldFromBody(response, "success", false);
        MethodsForCheckResponse.checkValueOfFieldFromBody(response, "message", "One or more ids provided are incorrect");
    }

    @Test
    @DisplayName("Create order without ingredients")
    @Description("Test for POST /api/orders")
    public void createOrderWithoutDataReturns400() {
        RequestCreateOrderDetails orderDetails = new RequestCreateOrderDetails(
            new String[] {}
        );

        Response response = methodsForTestsOrdersAPI.sendPostRequestApiOrder(orderDetails);

        MethodsForCheckResponse.compareStatusCode(response, SC_BAD_REQUEST);
        MethodsForCheckResponse.checkBooleanValueOfFieldFromBody(response, "success", false);
        MethodsForCheckResponse.checkValueOfFieldFromBody(response, "message", "Ingredient ids must be provided");
    }
}
