package diplomaproject.services.transactionService;

import diplomaproject.DTO.transaction.AddTransactionDTO;
import diplomaproject.DTO.transaction.EditTransactionDTO;
import diplomaproject.DTO.transaction.TransactionDTO;
import diplomaproject.models.Account;
import diplomaproject.models.MyTransaction;
import diplomaproject.repositories.AccountRepository;
import diplomaproject.repositories.TransactionRepository;
import diplomaproject.services.budgetService.BudgetService;
import diplomaproject.utils.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private static final int SIZE = 5;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final BudgetService budgetService;
    private final JwtToken jwtToken;

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactions(String token, int page) {
        String email = jwtToken.getEmail(token);
        Account account = accountRepository.findAccountByEmail(email);
        var transactions = transactionRepository.findByAccount_Id(account.getId(), PageRequest.of(page, SIZE, Sort.by(Sort.Order.desc("id")))).getContent();
        return transactions.stream().map(t -> t.toTransactionDTO()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addTransaction(String token, AddTransactionDTO addTransactionDTO) {
        String email = jwtToken.getEmail(token);
        budgetService.saveToBudget(addTransactionDTO, email);
        Account account = accountRepository.findAccountByEmail(email);
        MyTransaction myTransaction = MyTransaction.fromAddTransactionDTO(addTransactionDTO);
        account.getMyTransactions().add(myTransaction);
        myTransaction.setAccount(account);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteTransactionById(long id, String token) {
        String email = jwtToken.getEmail(token);
        MyTransaction transaction = transactionRepository.findById(id).get();
        budgetService.deleteFromBudget(transaction.getSum(), transaction.getType(), email);
        transactionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void editTransaction(EditTransactionDTO editTransactionDTO) {
        MyTransaction transaction = transactionRepository.findById(editTransactionDTO.getId()).get();

        budgetService.editBudget(transaction.getSum(),
                editTransactionDTO.getSum(),
                transaction.getType(),
                transaction.getAccount().getEmail());

        transaction.setDate(editTransactionDTO.getDate());
        transaction.setSum(editTransactionDTO.getSum());
        transaction.setDescription(editTransactionDTO.getDescription());

        transactionRepository.save(transaction);
    }
}
