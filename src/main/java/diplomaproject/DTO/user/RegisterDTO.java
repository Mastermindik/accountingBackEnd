package diplomaproject.DTO.user;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String email;
    private String password;

    public RegisterDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
