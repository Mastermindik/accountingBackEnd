package diplomaproject.DTO.category;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CategoriesDTO {
    private List<String> incomes;
    private List<String> expenses;

    public CategoriesDTO() {
        this.incomes = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }
}
