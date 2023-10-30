package diplomaproject.services.categoryService;

import diplomaproject.DTO.category.AddCategoryDTO;
import diplomaproject.DTO.category.CategoriesDTO;
import diplomaproject.DTO.category.CustomCategoryDTO;
import diplomaproject.DTO.category.DeleteCategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoriesDTO getAllCategories(String token);
    void addCategory(AddCategoryDTO categoryDTO, String token);
    boolean deleteCategoryById(long id);
    List<CustomCategoryDTO> getCustomCategories(String token);
}
