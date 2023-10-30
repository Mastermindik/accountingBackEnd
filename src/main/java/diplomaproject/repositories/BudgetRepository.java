package diplomaproject.repositories;

import diplomaproject.models.Account;
import diplomaproject.models.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Budget findBudgetByAccount_Email(String email);
}
