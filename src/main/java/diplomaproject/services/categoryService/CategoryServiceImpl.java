package diplomaproject.services.categoryService;

import diplomaproject.DTO.category.AddCategoryDTO;
import diplomaproject.DTO.category.CategoriesDTO;
import diplomaproject.DTO.category.CustomCategoryDTO;
import diplomaproject.models.*;
import diplomaproject.repositories.AccountRepository;
import diplomaproject.repositories.CustomCategoryRepository;
import diplomaproject.repositories.DefaultCategoryRepository;
import diplomaproject.utils.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final DefaultCategoryRepository defaultCategoryRepository;
    private final CustomCategoryRepository customCategoryRepository;
    private final AccountRepository accountRepository;
    private final JwtToken jwtToken;
    @Override
    @Transactional(readOnly = true)
    public CategoriesDTO getAllCategories(String token) {
        List<DefaultCategory> defaultCategories = defaultCategoryRepository.findAll();
        String email = jwtToken.getEmail(token);
        List<CustomCategory> customCategories = customCategoryRepository.findCustomCategoriesByAccount_Email(email);
        CategoriesDTO categoriesDTO = new CategoriesDTO();
        divideCategories(defaultCategories, categoriesDTO);
        divideCategories(customCategories, categoriesDTO);
        return categoriesDTO;
    }

    @Override
    @Transactional
    public void addCategory(AddCategoryDTO categoryDTO, String token) {
        String email = jwtToken.getEmail(token);
        Account account = accountRepository.findAccountByEmail(email);
        CustomCategory customCategory = CustomCategory.fromAddCategoryDTO(categoryDTO);
        if (account.getCustomCategories() == null) {
            account.setCustomCategories(new ArrayList<>());
        } else if (account.getCustomCategories().stream().anyMatch(c -> c.getName().equals(customCategory.getName()) && c.getType().equals(customCategory.getType()) )) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        account.getCustomCategories().add(customCategory);
        customCategory.setAccount(account);
//        account.addCustomCategory(customCategory);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteCategoryById(long id) {
        customCategoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomCategoryDTO> getCustomCategories(String token) {
        String email = jwtToken.getEmail(token);
        List<CustomCategory> categories = customCategoryRepository.findCustomCategoriesByAccount_Email(email);
        return categories.stream().map(c -> c.toCustomCategoryDTO()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void setUpDefaultCategories() {
        if (defaultCategoryRepository.findAll().size() == 0) {
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
        }
    }

    private void divideCategories(List<? extends MainCategory> categories, CategoriesDTO categoriesDTO) {

        for (var category: categories) {
            if (category.getType().name().equals("EXPENSE")) {
                categoriesDTO.getExpenses().add(category.getName());
            } else {
                categoriesDTO.getIncomes().add(category.getName());
            }
        }
    }
}
