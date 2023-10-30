package diplomaproject.services.categoryService;

import diplomaproject.DTO.category.AddCategoryDTO;
import diplomaproject.DTO.category.CategoriesDTO;
import diplomaproject.DTO.category.CustomCategoryDTO;
import diplomaproject.DTO.category.DeleteCategoryDTO;
import diplomaproject.models.Account;
import diplomaproject.models.DefaultCategory;
import diplomaproject.models.CustomCategory;
import diplomaproject.models.MainCategory;
import diplomaproject.repositories.AccountRepository;
import diplomaproject.repositories.DefaultCategoryRepository;
import diplomaproject.repositories.CustomCategoryRepository;
import diplomaproject.utils.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    public boolean deleteCategoryById(long id) {
//        customCategoryRepository.findById(id);
        customCategoryRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomCategoryDTO> getCustomCategories(String token) {
        String email = jwtToken.getEmail(token);
        List<CustomCategory> categories = customCategoryRepository.findCustomCategoriesByAccount_Email(email);
        return categories.stream().map(c -> c.toCustomCategoryDTO()).collect(Collectors.toList());
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
