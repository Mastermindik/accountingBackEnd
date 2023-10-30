package diplomaproject.config;

import diplomaproject.models.Account;
import diplomaproject.models.Budget;
import diplomaproject.repositories.AccountRepository;
import diplomaproject.utils.JwtToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthHandler implements AuthenticationSuccessHandler {
    private final AccountRepository accountRepository;
    private final JwtToken jwtToken;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        OAuth2User user = token.getPrincipal();

        Map<String, Object> attributes = user.getAttributes();

        Account account = new Account(
                attributes.get("name").toString(),
                attributes.get("email").toString(),
                null,
                attributes.get("picture").toString()
        );
        if (!accountRepository.existsUserByEmail(account.getEmail())) {
            Budget budget = new Budget(account);
            account.setBudget(budget);
            accountRepository.save(account);
        }
        String accountToken = jwtToken.generateToken(account);
        var cookie = new Cookie("jwt-token", accountToken);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60);
        response.addCookie(cookie);
        response.sendRedirect("https://accounting-sand.vercel.app/dashboard");
    }
}
