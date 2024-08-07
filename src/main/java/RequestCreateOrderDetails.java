import lombok.Data;

@Data
public class RequestCreateOrderDetails {
    private String[] ingredients;

    public RequestCreateOrderDetails(String[] ingredients) {
        this.ingredients = ingredients;
    }
}
