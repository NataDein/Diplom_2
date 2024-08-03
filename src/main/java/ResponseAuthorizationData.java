public class ResponseAuthorizationData extends ResponseUserData {
    protected static final int BEARER_TOKEN_BEGIN_INDEX = 7;

    String accessToken;
    String refreshToken;

    public String getAccessToken() {
        return accessToken.substring(BEARER_TOKEN_BEGIN_INDEX);
    }

    public void setAccessToken(String id) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String id) {
        this.refreshToken = refreshToken;
    }
}
