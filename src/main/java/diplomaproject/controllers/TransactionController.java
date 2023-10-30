package diplomaproject.controllers;

import diplomaproject.DTO.AppSuccess;
import diplomaproject.DTO.transaction.AddTransactionDTO;
import diplomaproject.DTO.transaction.EditTransactionDTO;
import diplomaproject.DTO.transaction.TransactionDTO;
import diplomaproject.exeptions.AppError;
import diplomaproject.services.transactionService.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@CrossOrigin
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/all/{page}")
    public List<TransactionDTO> getAll(@PathVariable(name = "page") int page,
                                       HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return transactionService.getTransactions(token, page);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody AddTransactionDTO addTransactionDTO,
                                 HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        transactionService.addTransaction(token, addTransactionDTO);
        return new ResponseEntity<>(new AppSuccess("Transaction added successfully"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") long id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        transactionService.deleteTransactionById(id, token);
        return new ResponseEntity<>(new AppSuccess("Transaction deleted successfully"), HttpStatus.OK);
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody EditTransactionDTO editTransactionDTO) {
        try {
            transactionService.editTransaction(editTransactionDTO);
            return new ResponseEntity<>(new AppSuccess("Transaction edited successfully"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Something was wrong"), HttpStatus.BAD_REQUEST);
        }
    }


}
