package diplomaproject.DTO.category;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomCategoryDTO {
    private long id;
    private String name;
    private String type;
}
