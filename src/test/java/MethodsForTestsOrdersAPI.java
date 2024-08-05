import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class MethodsForTestsOrdersAPI {
    protected String baseIngredientsEndpoint = "/api/ingredients";
    protected String baseOrdersEndpoint = "/api/orders";
    protected String allOrdersEndpoint = baseOrdersEndpoint + "/all";



    public Ingredient[] getData(Response response) {
        return response.body().as(ResponseIngredientsData.class).getData();
    }

    @Step("Send GET request to /api/auth/user")
    public Response sendGetRequestApiIngredients() {
        Response response =
            given()
                .header("Content-type","application/json")
                .get(baseIngredientsEndpoint);

        return response;
    }

    @Step("Send POST request to /api/orders")
    public Response sendPostRequestApiOrder(RequestCreateOrderDetails orderDetails, String accessToken) {
        Response response =
            given()
                .auth().oauth2(accessToken)
                .header("Content-type","application/json")
                .and()
                .body(orderDetails)
                .when()
                .post(baseOrdersEndpoint);

        return response;
    }

    @Step("Send POST request to /api/orders")
    public Response sendPostRequestApiOrder(RequestCreateOrderDetails orderDetails) {
        Response response =
            given()
                .header("Content-type","application/json")
                .and()
                .body(orderDetails)
                .when()
                .post(baseOrdersEndpoint);

        return response;
    }

    @Step("Send GET request to /api/orders/all")
    public Response sendGetRequestApiAllOrder() {
        Response response =
            given()
                .header("Content-type","application/json")
                .get(allOrdersEndpoint);

        return response;
    }

    @Step("Send GET request to /api/orders")
    public Response sendGetRequestApiOrder(String accessToken) {
        Response response =
            given()
                .auth().oauth2(accessToken)
                .header("Content-type","application/json")
                .get(baseOrdersEndpoint);

        return response;
    }

    @Step("Send GET request to /api/orders")
    public Response sendGetRequestApiOrder() {
        Response response =
            given()
                .header("Content-type","application/json")
                .get(baseOrdersEndpoint);

        return response;
    }
}
