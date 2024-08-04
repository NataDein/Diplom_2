import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class MethodsForTestsUserAPI {
    protected String baseAuthUserEndpoint = "/api/auth";
    protected String userEndpoint = baseAuthUserEndpoint + "/user";
    protected String registerUserEndpoint = baseAuthUserEndpoint + "/register";
    protected String loginUserEndpoint = baseAuthUserEndpoint + "/login";


    @Step("Send POST request to /api/auth/register")
    public Response sendPostRequestApiAuthRegister(User user) {
        Response response =
            given()
                .header("Content-type","application/json")
                .and()
                .body(user)
                .when()
                .post(registerUserEndpoint);

        return response;
    }

    @Step("Send POST request to /api/auth/login")
    public Response sendPostRequestApiAuthLogin(User user) {
        Response response =
            given()
                .header("Content-type","application/json")
                .and()
                .body(user)
                .when()
                .post(loginUserEndpoint);

        return response;
    }

    @Step("Send DELETE request to /api/auth/user")
    public Response sendDeleteRequestApiAuthUser(String userToken) {
        Response response =
            given()
                .auth().oauth2(userToken)
                .header("Content-type","application/json")
                .delete(userEndpoint);

        return response;
    }

    @Step("Send GET request to /api/auth/user")
    public Response sendGetRequestApiAuthUser(String userToken) {
        Response response =
            given()
                .auth().oauth2(userToken)
                .header("Content-type","application/json")
                .get(userEndpoint);

        return response;
    }

    @Step("Send PATCH request to /api/auth/user")
    public Response sendPatchRequestApiAuthUser(User userData, String accessToken) {
        Response response =
            given()
                .auth().oauth2(accessToken)
                .header("Content-type","application/json")
                .and()
                .body(userData)
                .when()
                .patch(userEndpoint);

        return response;
    }

    @Step("Send unauthorized PATCH request to /api/auth/user")
    public Response sendPatchRequestApiAuthUser(User userData) {
        Response response =
            given()
                .header("Content-type","application/json")
                .and()
                .body(userData)
                .when()
                .patch(userEndpoint);

        return response;
    }
}
