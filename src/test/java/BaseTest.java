import io.restassured.RestAssured;
import org.junit.Before;

public class BaseTest {
    protected MethodsForTestsUserAPI methodsForTestsUserAPI;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
}
