import lombok.Data;

@Data
public class RequestCreateOrderDetails {
    String[] ingredients;

    public RequestCreateOrderDetails(String[] ingredients) {
        this.ingredients = ingredients;
    }
}
