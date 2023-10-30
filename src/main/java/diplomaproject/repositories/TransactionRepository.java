package diplomaproject.repositories;

import diplomaproject.models.Account;
import diplomaproject.models.MyTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<MyTransaction, Long> {
    Page<MyTransaction> findByAccount_Id(long id, Pageable pageable);
    List<MyTransaction> findAllByAccount_EmailAndDateBetween(String email, Date startDate, Date endDate);
    List<MyTransaction> findAllByAccountAndDateBetween(Account email, Date startDate, Date endDate);
}
