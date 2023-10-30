package diplomaproject.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BudgetDTO {
    private Double total;
    private Double expenses;
    private Double incomes;
    private Double increaseExpenses;
    private Double increaseIncomes;
}
