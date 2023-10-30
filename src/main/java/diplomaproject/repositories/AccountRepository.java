package diplomaproject.repositories;

import diplomaproject.models.Account;
import diplomaproject.models.MyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsUserByEmail(String email);
    Account findAccountByEmail(String email);
}
