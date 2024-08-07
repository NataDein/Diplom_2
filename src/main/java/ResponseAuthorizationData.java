import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseAuthorizationData extends ResponseUserData {
    protected static final int BEARER_TOKEN_BEGIN_INDEX = 7;

    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken.substring(BEARER_TOKEN_BEGIN_INDEX);
    }
}
