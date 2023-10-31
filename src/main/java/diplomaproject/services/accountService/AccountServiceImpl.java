package diplomaproject.services.accountService;

import diplomaproject.DTO.user.AccountDTO;
import diplomaproject.DTO.user.JwtRequest;
import diplomaproject.DTO.user.RegisterDTO;
import diplomaproject.DTO.user.UpdateAccountDTO;
import diplomaproject.models.Account;
import diplomaproject.models.Budget;
import diplomaproject.repositories.AccountRepository;
import diplomaproject.services.accountPictureService.AccountPictureService;
import diplomaproject.utils.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountPictureService accountPictureService;
    private final PasswordEncoder passwordEncoder;
    private final JwtToken jwtToken;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AccountDTO getAccountFromToken(String token) {
        String email = jwtToken.getEmail(token.substring(7));
        return accountRepository.findAccountByEmail(email).toAccountDTO();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream().map(u -> u.toAccountDTO()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public String loginAccount(JwtRequest jwtRequest) throws BadCredentialsException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword()));
        Account account = accountRepository.findAccountByEmail(jwtRequest.getEmail());
        return jwtToken.generateToken(account);
    }

    @Override
    @Transactional(readOnly = true)
    public void logoutAccount(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization").substring(7);
        jwtToken.getAllClaims(token).setExpiration(new Date());
    }

    @Override
    @Transactional
    public String registerAccount(RegisterDTO registerDTO) {
        if (accountRepository.existsUserByEmail(registerDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        Account account = Account.fromRegisterDTO(registerDTO);
        Budget budget = new Budget(account);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setBudget(budget);
        accountRepository.save(account);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registerDTO.getEmail(), registerDTO.getPassword()));
        return jwtToken.generateToken(account);
    }

    @Override
    @Transactional
    public void updateAccount(UpdateAccountDTO updateAccountDTO, String token) {
        String email = jwtToken.getEmail(token);
        Account account = accountRepository.findAccountByEmail(email);
        if (account.getPassword().equals(passwordEncoder.encode(updateAccountDTO.getPassword()))) {
            if (updateAccountDTO.getNewUsername() != null) {
                account.setUsername(updateAccountDTO.getNewUsername());
            }
            if (updateAccountDTO.getNewPassword() != null) {
                account.setPassword(passwordEncoder.encode(updateAccountDTO.getPassword()));
            }
            accountRepository.save(account);
        } else {
            throw new BadCredentialsException("Incorrect password");
        }
    }

    @Override
    @Transactional
    public void registerAdmin(Account account) {
        if (!accountRepository.existsUserByEmail(account.getEmail())) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            Budget budget = new Budget(account);
            account.setBudget(budget);
            accountRepository.save(account);
        }
    }

    @Override
    @Transactional
    public void updateAccountPicture(MultipartFile file, String token) throws IOException {
        String email = jwtToken.getEmail(token);
        Account account = accountRepository.findAccountByEmail(email);
        long pictId = accountPictureService.saveAccountPicture(file);
        account.setPictureUrl("https://accounting-sand.vercel.app/user/image/" + pictId);
        accountRepository.save(account);
    }
}
