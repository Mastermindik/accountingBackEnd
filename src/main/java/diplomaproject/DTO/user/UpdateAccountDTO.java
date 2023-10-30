package diplomaproject.DTO.user;

import lombok.Data;

@Data
public class UpdateAccountDTO {
    private String password;
    private String newPassword;
    private String newUsername;
}
