import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseIngredientsData extends ResponseBaseData {
    Ingredient[] data;
}
