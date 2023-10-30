package diplomaproject.config;

import diplomaproject.exeptions.AppError;
import diplomaproject.models.Account;
import diplomaproject.models.DefaultCategory;
import diplomaproject.models.CustomRoles;
import diplomaproject.models.TransactionType;
import diplomaproject.repositories.DefaultCategoryRepository;
import diplomaproject.services.accountService.AccountDetailsService;
import diplomaproject.services.accountService.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final AccountDetailsService accountDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final AuthHandler authHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(accountDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/user/register").permitAll()
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/user/test").permitAll()
                        .requestMatchers("/user/image/*").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login(o -> o.successHandler(authHandler))
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt()
//                .oauth2Login(oauth -> oauth.successHandler(authenticationSuccessHandler))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CommandLineRunner addAdmin(final AccountServiceImpl accountService,
                                      final DefaultCategoryRepository defaultCategoryRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Account admin = new Account("Admin", "admin@admin.com", "adminadmin");
                admin.setRoles(CustomRoles.ADMIN);
                accountService.registerAdmin(admin);
                defaultCategoryRepository.save(new DefaultCategory("Food", TransactionType.EXPENSE));
                defaultCategoryRepository.save(new DefaultCategory("Transport", TransactionType.EXPENSE));
                defaultCategoryRepository.save(new DefaultCategory("Rent", TransactionType.EXPENSE));
                defaultCategoryRepository.save(new DefaultCategory("Education", TransactionType.EXPENSE));
                defaultCategoryRepository.save(new DefaultCategory("Vacation", TransactionType.EXPENSE));
                defaultCategoryRepository.save(new DefaultCategory("Bills and payments", TransactionType.EXPENSE));
                defaultCategoryRepository.save(new DefaultCategory("Entertainment", TransactionType.EXPENSE));
                defaultCategoryRepository.save(new DefaultCategory("Clothes and shoes", TransactionType.EXPENSE));
                defaultCategoryRepository.save(new DefaultCategory("Others", TransactionType.EXPENSE));
                defaultCategoryRepository.save(new DefaultCategory("Salary", TransactionType.INCOME));
                defaultCategoryRepository.save(new DefaultCategory("Dividends", TransactionType.INCOME));
                defaultCategoryRepository.save(new DefaultCategory("Percentages", TransactionType.INCOME));
                defaultCategoryRepository.save(new DefaultCategory("Business income", TransactionType.INCOME));
                defaultCategoryRepository.save(new DefaultCategory("Others", TransactionType.INCOME));
                System.out.println(defaultCategoryRepository.findAll());
            }
        };
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
