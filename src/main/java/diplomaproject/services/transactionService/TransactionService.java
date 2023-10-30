package diplomaproject.services.transactionService;

import diplomaproject.DTO.transaction.AddTransactionDTO;
import diplomaproject.DTO.transaction.EditTransactionDTO;
import diplomaproject.DTO.transaction.TransactionDTO;

import java.util.List;

public interface TransactionService {
    List<TransactionDTO> getTransactions(String token,int page);
    void addTransaction(String token, AddTransactionDTO addTransactionDTO);
    void deleteTransactionById(long id, String token);
    void editTransaction(EditTransactionDTO editTransactionDTO);
}
