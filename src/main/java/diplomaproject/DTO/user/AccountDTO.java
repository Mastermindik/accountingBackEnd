package diplomaproject.DTO.user;

import diplomaproject.models.CustomRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private String username;
    private String email;
    private String pictureUrl;
    private CustomRoles roles;
    private BudgetDTO budget;
}
