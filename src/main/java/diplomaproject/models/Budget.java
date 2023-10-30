package diplomaproject.models;

import diplomaproject.DTO.user.BudgetDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Budget {
    @Id
    @GeneratedValue
    private Long id;

    private Double total;

    private Double expenses;

    private Double incomes;

    private Double increaseExpenses;

    private Double increaseIncomes;

    @OneToOne(mappedBy = "budget")
    private Account account;

    public Budget(Account account) {
        this.total = 0D;
        this.expenses = 0D;
        this.incomes = 0D;
        this.increaseExpenses = 0D;
        this.increaseIncomes = 0D;
        this.account = account;
    }

    public BudgetDTO toBudgetDTO() {
        return new BudgetDTO(
                this.total,
                this.expenses,
                this.incomes,
                this.increaseExpenses,
                this.increaseIncomes
        );
    }

}
