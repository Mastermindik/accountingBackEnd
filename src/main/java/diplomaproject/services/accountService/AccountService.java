package diplomaproject.services.accountService;

import diplomaproject.DTO.user.AccountDTO;
import diplomaproject.DTO.user.JwtRequest;
import diplomaproject.DTO.user.RegisterDTO;
import diplomaproject.DTO.user.UpdateAccountDTO;
import diplomaproject.models.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AccountService {
    AccountDTO getAccountFromToken(String token);
    List<AccountDTO> getAllAccounts();
    String loginAccount(JwtRequest jwtRequest) throws BadCredentialsException;
    void logoutAccount(HttpServletRequest request, HttpServletResponse response);
    String registerAccount(RegisterDTO registerDTO);
    void updateAccount(UpdateAccountDTO updateAccountDTO, String token);
    void registerAdmin(Account account);
    void updateAccountPicture(MultipartFile file, String token) throws IOException;
}
