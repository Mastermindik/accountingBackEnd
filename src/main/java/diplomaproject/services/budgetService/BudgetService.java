package diplomaproject.services.budgetService;

import diplomaproject.DTO.transaction.AddTransactionDTO;
import diplomaproject.DTO.user.BudgetDTO;
import diplomaproject.models.TransactionType;

public interface BudgetService {
    void saveToBudget(AddTransactionDTO addTransactionDTO, String email);
    void deleteFromBudget(double sum, TransactionType type, String email);
    void editBudget(double sum,double newSum, TransactionType type, String email);
}
