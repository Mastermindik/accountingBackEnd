package diplomaproject.DTO.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AddTransactionDTO {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "EEST")
    private Date date;
    private String description;
    private Double sum;
    private String category;
    private String type;
}
