package diplomaproject.DTO.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStatisticDTO {
    private Map<String, Double> expenses;
    private Map<String, Double> incomes;
}
