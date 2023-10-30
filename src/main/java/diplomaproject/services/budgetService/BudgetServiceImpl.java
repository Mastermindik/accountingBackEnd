package diplomaproject.services.budgetService;

import diplomaproject.DTO.transaction.AddTransactionDTO;
import diplomaproject.models.Budget;
import diplomaproject.models.TransactionType;
import diplomaproject.repositories.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;

    @Override
    @Transactional
    public void saveToBudget(AddTransactionDTO addTransactionDTO, String email) {
        Budget budget = budgetRepository.findBudgetByAccount_Email(email);
        if (addTransactionDTO.getType().equals("income")) {
            budget.setTotal(budget.getTotal() + addTransactionDTO.getSum());
            budget.setIncomes(budget.getIncomes() + addTransactionDTO.getSum());
        } else if (addTransactionDTO.getType().equals("expense")) {
            budget.setTotal(budget.getTotal() - addTransactionDTO.getSum());
            budget.setExpenses(budget.getExpenses() + addTransactionDTO.getSum());
        }
        budgetRepository.save(budget);
    }

    @Override
    @Transactional
    public void deleteFromBudget(double sum, TransactionType type,  String email) {
        Budget budget = budgetRepository.findBudgetByAccount_Email(email);
        if (type == TransactionType.EXPENSE) {
            budget.setTotal(budget.getTotal() + sum);
            budget.setExpenses(budget.getExpenses() - sum);
        } else if (type == TransactionType.INCOME) {
            budget.setTotal(budget.getTotal() - sum);
            budget.setIncomes(budget.getIncomes() - sum);
        }
    }

    @Override
    @Transactional
    public void editBudget(double sum, double newSum, TransactionType type, String email) {
        Budget budget = budgetRepository.findBudgetByAccount_Email(email);
        double difference = newSum - sum;
        budget.setTotal(budget.getTotal() + difference);
        if (type == TransactionType.EXPENSE) {
            budget.setExpenses(budget.getExpenses() + difference);
        } else if (type == TransactionType.INCOME) {
            budget.setIncomes(budget.getIncomes() + difference);
        }
    }
}
