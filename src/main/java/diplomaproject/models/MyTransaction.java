package diplomaproject.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import diplomaproject.DTO.transaction.AddTransactionDTO;
import diplomaproject.DTO.transaction.TransactionDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class MyTransaction {
    @Id
    @GeneratedValue
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String description;

    private Double sum;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String category;

    public MyTransaction(Date date, String description, Double sum, TransactionType type, String category) {
        this.date = date;
        this.description = description;
        this.sum = sum;
        this.type = type;
        this.category = category;
    }

    public TransactionDTO toTransactionDTO() {
        return new TransactionDTO(id, date, description, sum, category, type.name().toLowerCase());
    }//!!!!!!!!!!!!!

    public static MyTransaction fromAddTransactionDTO(AddTransactionDTO addTransactionDTO) {
        return new MyTransaction(
                addTransactionDTO.getDate(),
                addTransactionDTO.getDescription(),
                addTransactionDTO.getSum(),
                TransactionType.valueOf(addTransactionDTO.getType().toUpperCase()),
                addTransactionDTO.getCategory());
    }

    @Override
    public String toString() {
        return "MyTransaction{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", sum=" + sum +
                ", type=" + type +
                ", category='" + category + '\'' +
                '}';
    }
}
