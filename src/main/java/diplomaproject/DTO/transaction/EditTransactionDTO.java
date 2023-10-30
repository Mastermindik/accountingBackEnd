package diplomaproject.DTO.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class EditTransactionDTO {
    private long id;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "EEST")
    private Date date;
    private String description;
    private Double sum;
}
