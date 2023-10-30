package diplomaproject.models;

import diplomaproject.DTO.user.RegisterDTO;
import diplomaproject.DTO.user.AccountDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @Column(nullable = false)
    private String email;

    private String password;

    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    private CustomRoles roles;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<MyTransaction> myTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<CustomCategory> customCategories = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "budget_id")
    private Budget budget;

    public Account() {
    }

    public Account(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = CustomRoles.USER;
    }

    public Account(String username, String email, String password, String pictureUrl) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.pictureUrl = pictureUrl;
        this.roles = CustomRoles.USER;
    }

    public static Account fromRegisterDTO(RegisterDTO registerDTO) {
        return new Account(registerDTO.getUsername(), registerDTO.getEmail(), registerDTO.getPassword());
    }

    public AccountDTO toAccountDTO() {
        return new AccountDTO(username, email, pictureUrl, roles, budget.toBudgetDTO());
    }

    public void addCustomCategory(CustomCategory category) {
        if (!customCategories.contains(category)) {
            customCategories.add(category);
            category.setAccount(this);
        }
    }

}
