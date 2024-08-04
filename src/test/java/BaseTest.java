import io.restassured.RestAssured;
import org.junit.Before;

public class BaseTest {
    protected MethodsForTestsUserAPI methodsForTestsUserAPI;
    protected MethodsForTestsOrdersAPI methodsForTestsOrdersAPI;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
}
