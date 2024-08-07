import io.restassured.RestAssured;
import org.junit.Before;

public class BaseTest {
    protected static final String BASE_URI = "https://stellarburgers.nomoreparties.site";

    protected MethodsForTestsUserAPI methodsForTestsUserAPI;
    protected MethodsForTestsOrdersAPI methodsForTestsOrdersAPI;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }
}
